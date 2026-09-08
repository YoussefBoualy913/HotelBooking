package service;
import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import model.User;
import repository.UserRepository;
import util.*;

import java.util.Optional;

public class AuthService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(
            String fullName,
            String email,
            String phone,
            String password
    ) {
        if (!ValidationUtils.isNotEmpty(fullName)) {
            throw new IllegalArgumentException("Full Name cannot be empty");
        }


        if (!ValidationUtils.isValidEmail(email)) {
           throw new IllegalArgumentException("Email must contain a '@' ");
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (!ValidationUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("password must contain at least 6 characters");
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        User user = new User(
                fullName,
                email,
                phone,
                password
        );

        userRepository.save(user);

        return user;
    }

    public User login(String email, String password) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }

        if (!user.get().getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }

        currentUser = user.get();

        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void updateProfile(String fullName, String email, String phone) {

        User user = getCurrentUser();

        if (!ValidationUtils.isNotEmpty(fullName)) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone");
        }

        if (!user.getEmail().equalsIgnoreCase(email)
                && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
    }

    public void changePassword(String oldPassword, String newPassword) {

        User user = getCurrentUser();


        if (!user.getPassword().equals(oldPassword)) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }


        if (!ValidationUtils.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(
                    "New password must contain at least 6 characters"
            );
        }

        user.setPassword(newPassword);
    }
}