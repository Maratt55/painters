package com.test.service.interfaces;

import com.test.exceptions.NotFoundException;
import com.test.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void saveUser(User user) throws NotFoundException;

    User getById(int id) throws NotFoundException;

    void delete(int id);

    User getByEmail(String email) throws NotFoundException;

    Page<User> getAll(Pageable pageable);

    void register(User user) throws NotFoundException;

    void verify(String email, String verification) throws NotFoundException;

    User login(String email, String password) throws NotFoundException;

    void resetPassword(String email, String resetPasswordCode, String newPassword) throws NotFoundException;

    void endPoint(String email) throws NotFoundException;

    User getByVerificationCode(String verificationCode);

    User getByPasswordCode(String resetPasswordCode);
}