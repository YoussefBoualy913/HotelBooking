package model;
import java.util.UUID;
public class User {

    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String password;

    public User(  String fullName, String email, String phone ,String password) {

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.id = UUID.randomUUID();
        this.password = password;
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


}