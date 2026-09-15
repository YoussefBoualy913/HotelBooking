package service;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.UnauthorizedException;
import model.User;
import repository.UserRepository;
import util.InputUtils;
import util.ValidationUtils;

import java.util.List;

public class UserService {
    private UserRepository userRepository;
    private AuthService authService;
    private InputUtils inputUtils;

    public UserService(UserRepository userRepository, AuthService authService, InputUtils inputUtils) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.inputUtils = inputUtils;

    }
    public void updateProfile() {
        User currentUser = authService.getCurrentUser();
        String fullName =  inputUtils.readString("Fullname:");
        if (!ValidationUtils.isNotEmpty(fullName)) {
            throw new IllegalArgumentException("Full name is required");
        }

        String email =  inputUtils.readString("Email:");
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!currentUser.getEmail().equalsIgnoreCase(email)
                && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String phone =  inputUtils.readString("Phone:");
        if (!ValidationUtils.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone");
        }

        currentUser.setFullName(fullName);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        System.out.println("Profile updated successfully");
    }

    public void changePassword() {
        User currentUser = authService.getCurrentUser();
        String oldPassword =  inputUtils.readString("oldPassword:");
        if (!currentUser.getPassword().equals(oldPassword)) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        String newPassword =  inputUtils.readString("newPassword:");
        if (!ValidationUtils.isValidPassword(newPassword)) {
            throw new IllegalArgumentException(
                    "New password must contain at least 6 characters"
            );
        }

        currentUser.setPassword(newPassword);
        System.out.println("Password changed successfully");
    }
    public List<User> getAllUsers() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new UnauthorizedException(
                    "Only administrators can view all users."
            );
        }

        return userRepository.findAll();
    }
}
