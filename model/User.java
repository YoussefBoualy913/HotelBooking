package model;
import java.util.UUID;
public class User {

    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private UserRole role;

    public User(  String fullName, String email, String phone ,String password) {

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.id = UUID.randomUUID();
        this.password = password;
        this.role = UserRole.CLIENT;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isAdmin(){
        return role == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }



}