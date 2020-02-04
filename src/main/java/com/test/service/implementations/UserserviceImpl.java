package com.test.service.implementations;

import com.test.enums.Status;
import com.test.exceptions.DuplicateException;
import com.test.exceptions.InvalidParamException;
import com.test.exceptions.NotFoundException;
import com.test.model.User;
import com.test.repository.UserRepository;
import com.test.service.interfaces.EmailService;
import com.test.service.interfaces.UserService;
import net.bytebuddy.utility.RandomString;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserserviceImpl implements UserService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserserviceImpl.class);

    public static final long CURRENTY_FOR_HOURS = 12 * 60 * 60 * 1000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveUser(User user) throws NotFoundException {

        User user1 = getByEmail(user.getEmail());
        if (user1 != null) {
            throw new DuplicateException("Duplicated user data");
        }
        userRepository.save(user);
    }

    public User getById(int id) throws NotFoundException {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new NotFoundException("user not found");
        }
        return user;
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public User getByEmail(String email) throws NotFoundException {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            throw new NotFoundException("User is not found");
        }
        return user;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page;
    }


    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void register(User user) throws NotFoundException {
        User user1 = getByEmail(user.getEmail());
        if (user1 != null) {
            throw new DuplicateException("Duplicated user data");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setVerificationTime(System.currentTimeMillis());
        user.setStatus(Status.UNVERIFIED);
        setVerifyCode(user);
        userRepository.save(user);
    }

    @Transactional
    public void verify(String email, String verification) throws NotFoundException {
        User user = getByEmail(email);
        if (verification.equals(user.getVerificationCode())) {
            if ((System.currentTimeMillis() - user.getVerificationTime()) >= CURRENTY_FOR_HOURS) {
                setVerifyCode(user);
                userRepository.save(user);
            } else {
                user.setStatus(Status.VERIFIED);
                user.setVerificationCode(null);
                userRepository.save(user);
            }
        } else
            throw new InvalidParamException("Verification code is false");
    }

    private User setVerifyCode(User user) {
        RandomString randomString = new RandomString();
        String verificationCode = randomString.nextString();
        while (getByVerificationCode(verificationCode) != null) {
            verificationCode = randomString.nextString();
        }
        user.setVerificationCode(verificationCode);
        user.setVerificationTime(System.currentTimeMillis());
        String text = ("Your verification code is: " + verificationCode);
        emailService.sendSimpleMessage(user.getEmail(), "Please verify your email", text);
        return user;
    }

    public User login(String email, String password) throws NotFoundException {
        User user = getByEmail(email);
        if (encoder.matches(password, user.getPassword())) {
            if (user.getStatus() == Status.UNVERIFIED) {
                throw new InvalidParamException("Confirm verification code");
            }
            return user;
        } else
            throw new InvalidParamException("incorrect email or password");
    }

    @Transactional
    public void endPoint(String email) throws NotFoundException {
        User user = getByEmail(email);
        setPasswordCode(user);
        user.setStatus(Status.UNVERIFIED);
        userRepository.save(user);
    }

    private User setPasswordCode(User user) {
        RandomString randomString = new RandomString();
        String passwordCode = randomString.nextString();
       /* while (getByPasswordCode(passwordCode) != null) {
            passwordCode = randomString.nextString();
        }*/
        user.setResetPasswordCode(randomString.nextString());
        user.setResetPasswordTime(System.currentTimeMillis());
        String text = ("Your verification code is: " + passwordCode);
        emailService.sendSimpleMessage(user.getEmail(), "Please verify your email", text);
        return user;
    }

    @Transactional
    public void resetPassword(String email, String resetPasswordCode, String newPassword) throws NotFoundException {
        User user = getByEmail(email);
        if (resetPasswordCode.equals(user.getResetPasswordCode())) {
            if ((System.currentTimeMillis() - user.getResetPasswordTime()) >= CURRENTY_FOR_HOURS) {
                setPasswordCode(user);
                userRepository.save(user);
            } else {
                user.setPassword(encoder.encode(newPassword));
                user.setVerificationTime(System.currentTimeMillis());
                user.setStatus(Status.VERIFIED);
                user.setVerificationCode(null);
                user.setResetPasswordCode(null);
                userRepository.save(user);
            }
        } else
            throw new InvalidParamException("confirm resetPasswordCode");
    }

    public User getByVerificationCode(String verificationCode) {
        User user = userRepository.getByVerificationCode(verificationCode);
        return user;
    }

    @Override
    public User getByPasswordCode(String passwordCode) {
        User user = userRepository.getByResetPasswordCode(passwordCode);
        return user;
    }
}
