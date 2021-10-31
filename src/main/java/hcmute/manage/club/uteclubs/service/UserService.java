package hcmute.manage.club.uteclubs.service;

import hcmute.manage.club.uteclubs.exception.*;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithOTPParams;
import hcmute.manage.club.uteclubs.framework.dto.user.UserSignUpWithoutOTPParams;
import hcmute.manage.club.uteclubs.model.Mail;
import hcmute.manage.club.uteclubs.model.Role;
import hcmute.manage.club.uteclubs.model.User;
import hcmute.manage.club.uteclubs.repository.RoleRepository;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static hcmute.manage.club.uteclubs.utility.DateUtility.differenceInYear;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private ApplicationContext context;
    private final OtpService otpService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getUserRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    public User validateInfoAndGenerateOTP(UserSignUpWithoutOTPParams params)
            throws ConflictWorkFlowException, DateException, PasswordsDoNotMatchException {
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

        if (userRepository.existsByStudentId(studentId)) {
            throw new ConflictWorkFlowException("This id is existing");
        }

        Date dob;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            dob = simpleDateFormat.parse(dobInString);
        } catch (ParseException exception) {
            throw new DateException("Wrong date format");
        }

        long age = differenceInYear(dob);
        if (age <= 17) {
            throw new DateException("You must be older than 17 years old");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ConflictWorkFlowException("This email is taken");
        }

        if (userRepository.existsByUsername(username)) {
            throw new ConflictWorkFlowException("This username is taken");
        }

        if (!password.equals(confirmedPassword)) {
            throw new PasswordsDoNotMatchException("These passwords do not match");
        }

        int otp = otpService.generateOTP(username);
        sendMail(fullName, email, otp);
        log.info("OTP is " + otp);

        User user = new User();
        user.setFullName(fullName);
        user.setStudentId(studentId);
        user.setGender(gender);
        user.setDob(dob);
        user.setFaculty(faculty);
        user.setMajor(major);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    public User addNewUser(UserSignUpWithOTPParams params) throws OtpException, ParseException {
        int inputOtp = Integer.parseInt(params.getOtp());
        int serverOtp = otpService.getOtp(params.getUsername());
        if (serverOtp > 0) {
            if (inputOtp != serverOtp) {
                throw new OtpException("This code is wrong. Please try again");
            }
            otpService.clearOTP(params.getUsername());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            User user = new User();

            user.setFullName(params.getFullName());
            user.setStudentId(params.getStudentId());
            user.setGender(params.getGender());
            user.setDob(simpleDateFormat.parse(params.getDob()));
            user.setFaculty(params.getFaculty());
            user.setMajor(params.getMajor());
            user.setEmail(params.getEmail());
            user.setUsername(params.getUsername());
            user.setPassword(passwordEncoder.encode(params.getPassword()));

            Role defaultRole = roleRepository.findRoleByRoleName("ROLE_USER");
            Collection<Role> roles = new ArrayList<>();
            roles.add(defaultRole);
            user.setUserRoles(roles);

            return userRepository.save(user);
        } else {
            throw new OtpException("This code is expired. Please get a new code");
        }
    }

    public User getUserById(Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return user.get();
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

    private void addRoleToUser(String username, String roleName) {
        User user = userRepository.findUserByUsername(username);
        Role role = roleRepository.findRoleByRoleName(roleName);
        user.getUserRoles().add(role);
    }
}
