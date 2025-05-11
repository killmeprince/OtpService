package org.example.otpservice.dto;

public class RegistrationRequest {
    private String username;
    private String password;
    private String role; //can be admin or user, mb more in future idk

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
