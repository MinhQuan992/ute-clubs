package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.InvalidRequestException;
import hcmute.manage.club.uteclubs.exception.NoContentException;
import hcmute.manage.club.uteclubs.exception.NotFoundException;
import hcmute.manage.club.uteclubs.exception.PermissionException;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAcceptOrRejectMemberParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddOrUpdateInfoParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubAddPersonParams;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import hcmute.manage.club.uteclubs.framework.dto.user_club.UserClubResponse;
import hcmute.manage.club.uteclubs.mapper.ClubMapper;
import hcmute.manage.club.uteclubs.mapper.UserClubMapper;
import hcmute.manage.club.uteclubs.mapper.UserMapper;
import hcmute.manage.club.uteclubs.model.Club;
import hcmute.manage.club.uteclubs.model.Mail;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.model.UserClub;
import hcmute.manage.club.uteclubs.repository.ClubRepository;
import hcmute.manage.club.uteclubs.repository.UserClubRepository;
import hcmute.manage.club.uteclubs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant.CLUB_NOT_FOUND;
import static hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant.USER_NOT_FOUND;
import static hcmute.manage.club.uteclubs.utility.UserUtility.getCurrentUsername;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ClubService {
    @Autowired
    private ApplicationContext context;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final UserClubRepository userClubRepository;

    public Page<ClubResponse> getClubs(Optional<Integer> page) {
        Page<Club> result = clubRepository.findAll(PageRequest.of(page.orElse(0), 10));
        if (result.isEmpty()) {
            throw new NoContentException();
        }
        return result.map(ClubMapper.INSTANCE::clubToClubDTO);
    }

    public List<ClubResponse> getManagedClubs() {
        String currentUsername = getCurrentUsername();
        User user = userRepository.findUserByUsername(currentUsername).get();
        return ClubMapper.INSTANCE.listClubToListClubDTO(userClubRepository.getManagedClubs(user));
    }

    public ClubResponse getClubDTOById(String clubId, boolean isLeaderOrMod) {
        Club club = getClubById(clubId);
        if (isLeaderOrMod) {
            validateLeaderModPermissions(club);
        }
        return ClubMapper.INSTANCE.clubToClubDTO(club);
    }

    public ClubResponse addNewClub(ClubAddOrUpdateInfoParams params) {
        Club club = new Club();
        club.setClubName(params.getClubName());
        club.setAffiliatedUnit(params.getAffiliatedUnit());
        club.setDescription(params.getDescription());
        return ClubMapper.INSTANCE.clubToClubDTO(clubRepository.save(club));
    }

    public UserClubResponse addPersonToClub(String clubId, ClubAddPersonParams params, boolean isAdmin) {
        Club club = getClubById(clubId);
        String role = params.getRoleInClub();

        if (!isAdmin) {
            validateLeaderPermission(club);
            if (role.equals("ROLE_LEADER")) {
                throw new PermissionException("You cannot add LEADER to your club");
            }
        }

        User user = getUserByStudentId(params.getStudentId());
        UserClub userClub;
        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(user, club);
        if (userClubOptional.isPresent()) {
            userClub = userClubOptional.get();
            if (userClub.isAccepted()) {
                throw new InvalidRequestException("This user was already in this club");
            }

            if (role.equals("ROLE_LEADER") && hasLeader(club)) {
                throw new InvalidRequestException("This club already had a LEADER");
            }

            userClub.setRoleInClub(role);
            userClub.setAccepted(true);
        } else {
            if (role.equals("ROLE_LEADER") && hasLeader(club)) {
                throw new InvalidRequestException("This club already had a LEADER");
            }

            userClub = new UserClub(user, club, role, true);
        }

        UserClub result = userClubRepository.save(userClub);
        sendMail(user.getFullName(), user.getEmail(), club.getClubName(), role);
        return UserClubMapper.INSTANCE.userClubToUserClubDTO(result);
    }

    public Page<UserResponse> getMembers(String clubId, Optional<Integer> page) {
        Club club = getClubById(clubId);
        Page<User> result = userClubRepository.getMembers(club, PageRequest.of(page.orElse(0), 10));
        if (result.isEmpty()) {
            throw new NoContentException();
        }
        return result.map(UserMapper.INSTANCE::userToUserDTO);
    }

    public Page<UserResponse> getMemberRequests(String clubId, Optional<Integer> page) {
        Club club = getClubById(clubId);
        validateLeaderModPermissions(club);
        Page<User> result = userClubRepository.getMemberRequests(club, PageRequest.of(page.orElse(0), 10));
        if (result.isEmpty()) {
            throw new NoContentException();
        }
        return result.map(UserMapper.INSTANCE::userToUserDTO);
    }

    public String acceptMember(String clubId, ClubAcceptOrRejectMemberParam param) {
        Club club = getClubById(clubId);
        validateLeaderModPermissions(club);
        User user = getUserByStudentId(param.getStudentId());
        UserClub userClub = getUserClubByUserAndClub(user, club);

        if (userClub.isAccepted()) {
            throw new InvalidRequestException("This user is already in your club");
        }

        userClub.setAccepted(true);
        userClubRepository.save(userClub);

        return "This user has been accepted";
    }

    public String rejectMember(String clubId, ClubAcceptOrRejectMemberParam param) {
        Club club = getClubById(clubId);
        validateLeaderModPermissions(club);
        User user = getUserByStudentId(param.getStudentId());
        UserClub userClub = getUserClubByUserAndClub(user, club);

        if (userClub.isAccepted()) {
            throw new InvalidRequestException("This user is already in your club");
        }

        userClubRepository.delete(userClub);
        return  "This user has been rejected";
    }

    public ClubResponse updateInfo(String clubId, ClubAddOrUpdateInfoParams params) {
        Club club = getClubById(clubId);
        club.setClubName(params.getClubName());
        club.setAffiliatedUnit(params.getAffiliatedUnit());
        club.setDescription(params.getDescription());
        return ClubMapper.INSTANCE.clubToClubDTO(clubRepository.save(club));
    }

    private Club getClubById(String clubId) {
        Long idInNumber = Long.parseLong(clubId);
        Optional<Club> club = clubRepository.findById(idInNumber);
        if (club.isEmpty()) {
            throw new NotFoundException(CLUB_NOT_FOUND);
        }
        return club.get();
    }

    private User getUserByStudentId(String studentId) {
        Optional<User> userOptional = userRepository.findUserByStudentId(studentId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        return userOptional.get();
    }

    private UserClub getUserClubByUserAndClub(User user, Club club) {
        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(user, club);
        if (userClubOptional.isEmpty()) {
            throw new InvalidRequestException("This user doesn't request to join your club");
        }
        return userClubOptional.get();
    }

    private boolean hasLeader(Club club) {
        List<String> rolesInClub = userClubRepository.getRolesInClub(club);
        log.info("Role(s) in this club:");
        rolesInClub.forEach(log::info);
        return rolesInClub.stream().anyMatch(item -> item.equals("ROLE_LEADER"));
    }

    private void validateLeaderPermission(Club club) {
        String currentUsername = getCurrentUsername();
        User currentUser = userRepository.findUserByUsername(currentUsername).get();
        Optional<UserClub> userClub = userClubRepository.findUserClubByUserAndClub(currentUser, club);

        if (userClub.isEmpty()) {
            throw new PermissionException("You are not in this club");
        }

        if (!userClub.get().getRoleInClub().equals("ROLE_LEADER")) {
            throw new PermissionException("You are not allowed to do this action");
        }
    }

    private void validateLeaderModPermissions(Club club) {
        String currentUsername = getCurrentUsername();
        User currentUser = userRepository.findUserByUsername(currentUsername).get();
        Optional<UserClub> userClub = userClubRepository.findUserClubByUserAndClub(currentUser, club);

        if (userClub.isEmpty()) {
            throw new PermissionException("You are not in this club");
        }

        if (userClub.get().getRoleInClub().equals("ROLE_MEMBER")) {
            throw new PermissionException("You are not allowed to do this action");
        }
    }

    private void sendMail(String receiverName, String receiverMail, String club, String role) {
        String content = "Dear " + receiverName + ",\n"
                + "You have been added to " + club + ". Now you are a " + role.substring(5) + " of this club.";
        Mail mail = new Mail();
        mail.setMailFrom("uteclubs@gmail.com");
        mail.setMailTo(receiverMail);
        mail.setMailSubject("You have been added to a new club");
        mail.setMailContent(content);

        MailService mailService = (MailService) context.getBean("mailService");
        mailService.sendEmail(mail);
    }
}
