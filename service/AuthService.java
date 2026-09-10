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
    private InputUtils inputUtils;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.inputUtils = new InputUtils();
    }

    public User register() {





        String fullName =  inputUtils.readString("Fullname:");
        if (!ValidationUtils.isNotEmpty(fullName)) {
            throw new IllegalArgumentException("Full Name cannot be empty");
        }
        String email = inputUtils.readString("Email:");
        if (!ValidationUtils.isValidEmail(email)) {
           throw new IllegalArgumentException("Email must contain a '@' ");
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String phone = inputUtils.readString("Phone:");
        if (!ValidationUtils.isValidPhone(phone)) {
            throw new IllegalArgumentException("phone number invalide.");
        }

        String password = inputUtils.readString("Password:");
        if (!ValidationUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("password must contain at least 6 characters");
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

    public User login() {



        String email = inputUtils.readString("Email:");
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }
        String password = inputUtils.readString("Password:");
        if (!user.get().getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials.");
        }

        currentUser = user.get();

        return currentUser;
    }
    public User autoLogin() {
        Optional<User> user = userRepository.findByEmail("y@gmail.com");
        currentUser = user.get();

        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void updateProfile() {

        User user = getCurrentUser();

        String fullName =  inputUtils.readString("Fullname:");
        if (!ValidationUtils.isNotEmpty(fullName)) {
            throw new IllegalArgumentException("Full name is required");
        }

        String email =  inputUtils.readString("Email:");
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!user.getEmail().equalsIgnoreCase(email)
                && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String phone =  inputUtils.readString("Phone:");
        if (!ValidationUtils.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        System.out.println("Profile updated successfully");
    }

    public void changePassword() {

        User user = getCurrentUser();

        String oldPassword =  inputUtils.readString("oldPassword:");
        if (!user.getPassword().equals(oldPassword)) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        String newPassword =  inputUtils.readString("newPassword:");
        if (!ValidationUtils.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(
                    "New password must contain at least 6 characters"
            );
        }

        user.setPassword(newPassword);
        System.out.println("Password changed successfully");
    }
}