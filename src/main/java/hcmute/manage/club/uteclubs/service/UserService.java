package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.*;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubRegisterParam;
import hcmute.manage.club.uteclubs.framework.dto.club.ClubResponse;
import hcmute.manage.club.uteclubs.framework.dto.user.*;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static hcmute.manage.club.uteclubs.framework.common.ExceptionMessageConstant.*;
import static hcmute.manage.club.uteclubs.utility.DateUtility.differenceInYear;
import static hcmute.manage.club.uteclubs.utility.UserUtility.getCurrentUsername;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private ApplicationContext context;
    private final OtpService otpService;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        User user = userOptional.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    public UserResponse getUserDTOById(String id) {
        User user = getUserById(id);
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    public UserResponse validateInfoAndGenerateOTP(UserSignUpWithoutOTPParams params) {
        String fullName = params.getFullName();
        String studentId = params.getStudentId();
        String gender = params.getGender();
        String faculty = params.getFaculty();
        String major = params.getMajor();
        String dobInString = params.getDob();
        String username = params.getUsername();
        String password = params.getPassword();
        String confirmedPassword = params.getConfirmedPassword();
        String email = params.getEmail();
        String avatarUrl = params.getAvatarUrl();

        validateInfo(studentId, email, username, password, confirmedPassword);

        Date dob = parseDobFromStringToDate(dobInString);
        int otp = otpService.generateOTP(username);
        sendMail(fullName, email, otp);
        log.info("OTP is " + otp);

        User createdUser = createUser(fullName, studentId, gender, faculty, major,
                username, password, email, avatarUrl, dob);
        return UserMapper.INSTANCE.userToUserDTO(createdUser);
    }

    public UserResponse addNewUser(UserSignUpWithOTPParams params) {
        int inputOtp = Integer.parseInt(params.getOtp());
        int serverOtp = otpService.getOtp(params.getUsername());
        if (serverOtp > 0) {
            if (inputOtp != serverOtp) {
                throw new OtpException("This code is wrong. Please try again");
            }

            String fullName = params.getFullName();
            String studentId = params.getStudentId();
            String gender = params.getGender();
            String faculty = params.getFaculty();
            String major = params.getMajor();
            String dobInString = params.getDob();
            String username = params.getUsername();
            String password = params.getPassword();
            String email = params.getEmail();
            String avatarUrl = params.getAvatarUrl();

            validateInfo(studentId, email, username);
            Date dob = parseDobFromStringToDate(dobInString);

            User user = createUser(fullName, studentId, gender, faculty, major,
                    username, passwordEncoder.encode(password), email, avatarUrl, dob);
            user.setRole("ROLE_USER");
            //user.setRole("ROLE_ADMIN");
            otpService.clearOTP(params.getUsername());

            return UserMapper.INSTANCE.userToUserDTO(userRepository.save(user));
        } else {
            throw new OtpException("This code is expired. Please get a new code");
        }
    }

    public UserResponse updateUserInfo(UserUpdateInfoParams params) {
        User currentUser = getCurrentUser();

        String fullName = params.getFullName();
        String gender = params.getGender();
        String dobInString = params.getDob();
        String avatarUrl = params.getAvatarUrl();

        Date dob = parseDobFromStringToDate(dobInString);

        currentUser.setFullName(fullName);
        currentUser.setGender(gender);
        currentUser.setDob(dob);
        currentUser.setAvatarUrl(avatarUrl);

        return UserMapper.INSTANCE.userToUserDTO(userRepository.save(currentUser));
    }

    public UserResponse changePassword(UserChangePasswordParams params) {
        User currentUser = getCurrentUser();

        String oldPassword, newPassword, confirmedPassword;
        oldPassword = params.getOldPassword();
        newPassword = params.getNewPassword();
        confirmedPassword = params.getConfirmedPassword();

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new InvalidRequestException("The old password is incorrect");
        }

        if (!newPassword.equals(confirmedPassword)) {
            throw new PasswordsDoNotMatchException(PASSWORDS_DO_NOT_MATCH);
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        return UserMapper.INSTANCE.userToUserDTO(userRepository.save(currentUser));
    }

    public List<ClubResponse> getJoinedClubs() {
        User currentUser = getCurrentUser();
        List<Club> results = userClubRepository.getJoinedClubs(currentUser);
        if (results.isEmpty()) {
            throw new NoContentException();
        }
        return ClubMapper.INSTANCE.listClubToListClubDTO(results);
    }

    public List<ClubResponse> getNotJoinedClubs() {
        List<Club> allClubs = clubRepository.findAll();

        User currentUser = getCurrentUser();
        List<Club> joinedClubs = userClubRepository.getJoinedClubs(currentUser);

        List<Club> results = new ArrayList<>();
        allClubs.forEach(club -> {
            if (!joinedClubs.contains(club)) {
                results.add(club);
            }
        });
        if (results.isEmpty()) {
            throw new NoContentException();
        }
        return ClubMapper.INSTANCE.listClubToListClubDTO(results);
    }

    public String registerToClub(ClubRegisterParam param) {
        User currentUser = getCurrentUser();
        Club club = getClub(param.getClubId());

        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(currentUser, club);
        if (userClubOptional.isPresent()) {
            throw new InvalidRequestException("You have already registered to this club");
        }

        UserClub userClub = new UserClub(currentUser, club, "ROLE_MEMBER", false);
        userClubRepository.save(userClub);
        return "You have successfully registered to this club";
    }

    public String cancelRequest(String clubId) {
        User currentUser = getCurrentUser();
        Club club = getClub(clubId);

        Optional<UserClub> userClubOptional = userClubRepository.findUserClubByUserAndClub(currentUser, club);
        if (userClubOptional.isEmpty()) {
            throw new InvalidRequestException("You have not registered to this club");
        }

        UserClub userClub = userClubOptional.get();
        if (userClub.isAccepted()) {
            throw new InvalidRequestException("You have been accepted to be a member of this club");
        }

        userClubRepository.delete(userClub);
        return "Your request has been cancelled successfully";
    }

    private User getUserById(String id) {
        Long idInNumber = Long.parseLong(id);
        Optional<User> user = userRepository.findById(idInNumber);
        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        return user.get();
    }

    private User getCurrentUser() {
        String currentUsername = getCurrentUsername();
        return userRepository.findUserByUsername(currentUsername).get();
    }

    private Club getClub(String clubId) {
        Optional<Club> clubOptional = clubRepository.findById(Long.parseLong(clubId));
        if (clubOptional.isEmpty()) {
            throw new NotFoundException(CLUB_NOT_FOUND);
        }
        return clubOptional.get();
    }

    private void validateInfo(String studentId, String email, String username, String password, String confirmedPassword) {
        if (userRepository.existsByStudentId(studentId)) {
            throw new ResourceConflictException(STUDENT_ID_IS_EXISTING);
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("This email is taken");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResourceConflictException("This username is taken");
        }
        if (!password.equals(confirmedPassword)) {
            throw new PasswordsDoNotMatchException(PASSWORDS_DO_NOT_MATCH);
        }
    }

    private void validateInfo(String studentId, String email, String username) {
        if (userRepository.existsByStudentId(studentId)) {
            throw new ResourceConflictException(STUDENT_ID_IS_EXISTING);
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("This email is taken");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResourceConflictException("This username is taken");
        }
    }

    private Date parseDobFromStringToDate(String dobInString) {
        Date dob;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dob = simpleDateFormat.parse(dobInString);
            log.info("Date of birth: {}", dob);
        } catch (ParseException exception) {
            throw new DateException(WRONG_DATE_FORMAT);
        }
        long age = differenceInYear(dob);
        log.info("Age is: {}", age);
        if (age <= 17) {
            throw new UnderageException(UNDERAGE);
        }
        return dob;
    }

    private User createUser(String fullName, String studentId, String gender, String faculty, String major,
                            String username, String password, String email, String avatarUrl, Date dob) {
        User user = new User();

        user.setFullName(fullName);
        user.setStudentId(studentId);
        user.setGender(gender);
        user.setDob(dob);
        user.setFaculty(faculty);
        user.setMajor(major);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatarUrl(avatarUrl);

        return user;
    }

    private void sendMail(String receiverName, String receiverMail, int otp) {
        String content = "Dear " + receiverName + ",\n"
                + "This is your verification code: " + otp;
        Mail mail = new Mail();
        mail.setMailFrom("uteclubs@gmail.com");
        mail.setMailTo(receiverMail);
        mail.setMailSubject("UTE Clubs | Verification Code to Sign Up");
        mail.setMailContent(content);

        MailService mailService = (MailService) context.getBean("mailService");
        mailService.sendEmail(mail);
    }
}
