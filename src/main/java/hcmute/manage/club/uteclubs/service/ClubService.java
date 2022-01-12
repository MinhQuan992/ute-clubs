package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.*;
import hcmute.manage.club.uteclubs.framework.dto.club.*;
import hcmute.manage.club.uteclubs.framework.dto.user.UserResponse;
import hcmute.manage.club.uteclubs.mapper.ClubMapper;
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

import static hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant.*;
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
        User user = getCurrentUser();
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
        club.setLogoUrl(params.getLogoUrl());
        return ClubMapper.INSTANCE.clubToClubDTO(clubRepository.save(club));
    }

    public String addPersonToClub(String clubId, ClubAddPersonParams params, boolean isAdmin) {
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

        userClubRepository.save(userClub);
        sendMail(user.getFullName(), user.getEmail(), club.getClubName(), role);
        return "This user has been added successfully";
    }

    public String changeRole(String clubId, ClubChangeRoleParams params, boolean isAdmin) {
        Club club = getClubById(clubId);
        String role = params.getNewRole();

        if (!isAdmin) {
            validateLeaderPermission(club);
            if (role.equals("ROLE_LEADER")) {
                throw new PermissionException("You cannot change anyone's role to role LEADER");
            }
        }

        User user = getUserById(params.getUserId());
        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(user, club);
        if (userClubOptional.isEmpty() || !userClubOptional.get().isAccepted()) {
            throw new InvalidRequestException("This user is not in this club");
        }

        UserClub userClub = userClubOptional.get();
        if (userClub.getRoleInClub().equals(role)) {
            throw new InvalidRequestException("This user was already in this role");
        }

        boolean hasLeader = hasLeader(club);
        if (role.equals("ROLE_LEADER") && hasLeader) {
            throw new InvalidRequestException("This club already had a LEADER");
        }

        userClub.setRoleInClub(role);
        userClubRepository.save(userClub);
        return "The role of this user has been changed successfully";
    }

    public String removeMember(String clubId, String userId, boolean isAdmin) {
        Club club = getClubById(clubId);
        User user = getUserById(userId);

        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(user, club);
        if (userClubOptional.isEmpty() || !userClubOptional.get().isAccepted()) {
            throw new InvalidRequestException("This user is not in this club");
        }
        UserClub userClub = userClubOptional.get();

        if (!isAdmin) {
            validateLeaderPermission(club);
            if (userClub.getRoleInClub().equals("ROLE_LEADER")) {
                throw new PermissionException("You cannot do this action");
            }
        }
        userClubRepository.delete(userClub);
        return "This user has been removed from this club";
    }

    public String leaveClub(String clubId) {
        Club club = getClubById(clubId);
        User user = getCurrentUser();

        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(user, club);
        if (userClubOptional.isEmpty() || !userClubOptional.get().isAccepted()) {
            throw new InvalidRequestException("You are not in this club");
        }

        userClubRepository.delete(userClubOptional.get());
        return "You are no longer a member of this club";
    }

    public String getRoleInClubOfCurrentUser(String clubId) {
        Club club = getClubById(clubId);
        User user = getCurrentUser();

        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(user, club);
        if (userClubOptional.isEmpty() || !userClubOptional.get().isAccepted()) {
            throw new InvalidRequestException("You are not in this club");
        }

        return userClubOptional.get().getRoleInClub();
    }

    public List<UserResponse> getMembersByRole(String clubId, String role) {
        Club club = getClubById(clubId);
        List<User> result = userClubRepository.getMembersUsingRole(club, role);
        if (result.isEmpty()) {
            throw new NoContentException();
        }
        return UserMapper.INSTANCE.listUserToListUserDTO(result);
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

    public String acceptMember(String clubId, ClubAcceptMemberParam param) {
        Club club = getClubById(clubId);
        validateLeaderModPermissions(club);
        User user = getUserById(param.getUserId());
        UserClub userClub = getUserClubByUserAndClub(user, club);

        if (userClub.isAccepted()) {
            throw new InvalidRequestException("This user is already in your club");
        }

        userClub.setAccepted(true);
        userClubRepository.save(userClub);

        return "This user has been accepted";
    }

    public String rejectMember(String clubId, String userId) {
        Club club = getClubById(clubId);
        validateLeaderModPermissions(club);
        User user = getUserById(userId);
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
        club.setLogoUrl(params.getLogoUrl());
        return ClubMapper.INSTANCE.clubToClubDTO(clubRepository.save(club));
    }

    private User getCurrentUser() {
        String currentUsername = getCurrentUsername();
        Optional<User> userOptional = userRepository.findUserByUsername(currentUsername);
        if (userOptional.isEmpty()) {
            throw new AccessTokenException(INVALID_OR_MISSED_ACCESS_TOKEN);
        }
        return userOptional.get();
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

    private User getUserById(String userId) {
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
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
        User user = getCurrentUser();
        Optional<UserClub> userClub = userClubRepository.findUserClubByUserAndClub(user, club);

        if (userClub.isEmpty()) {
            throw new PermissionException("You are not in this club");
        }

        if (!userClub.get().getRoleInClub().equals("ROLE_LEADER")) {
            throw new PermissionException("You are not allowed to do this action");
        }
    }

    private void validateLeaderModPermissions(Club club) {
        User user = getCurrentUser();
        Optional<UserClub> userClub = userClubRepository.findUserClubByUserAndClub(user, club);

        if (userClub.isEmpty()) {
            throw new PermissionException("You are not in this club");
        }

        if (userClub.get().getRoleInClub().equals("ROLE_MEMBER")) {
            throw new PermissionException("You are not allowed to do this action");
        }
    }

    private void sendMail(String receiverName, String receiverMail, String club, String role) {
        String content = "<!DOCTYPE html><html><head><style>p, h2 {font-family: sans-serif;}\n" +
                "span {font-weight: bold;}</style></head><body>\n" +
                "<p>Hi <span>" + receiverName + "</span>,</p>\n" +
                "<p>You have just been added to <span>" + club + "</span>. " +
                "Now you are a <span>" + role.substring(5) + "</span> of this club.</p>\n" +
                "<p>We hope you will enjoy great moments with your teammates in your club!</p>\n" +
                "<p style=\"font-weight: bold;\">The UTE Clubs Team</p></body></html>";
        Mail mail = new Mail();
        mail.setMailFrom("uteclubs@gmail.com");
        mail.setMailTo(receiverMail);
        mail.setMailSubject("You have been added to a new club");
        mail.setMailContent(content);

        MailService mailService = (MailService) context.getBean("mailService");
        mailService.sendEmail(mail);
    }
}
