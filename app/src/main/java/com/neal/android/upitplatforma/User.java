package com.neal.android.upitplatforma;


/**
 * Created by Loredana on 27.09.2018.
 */

public class User {
    private String Username;
    private String Email;
    private String password;
    private String Imgprofi;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String Img) {
        Username = username;
        Email = email;
        Imgprofi = Img;

    }

    public User(String Username, String Email, String password, String Imgprofi) {
        this.Username = Username;
        this.Email = Email;
        this.password = password;
        this.Imgprofi = Imgprofi;
    }

    public User(String Username, String Email) {
        this.Username = Username;
        this.Email = Email;
    }

    public void setUsename(String Username) {
        this.Username = Username;
    }

    public String getUsername() {
        return Username;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getEmail() {
        return Email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setImgprofi(String Imgprofi) {
        this.Imgprofi = Imgprofi;
    }

    public String getImgprofil() {
        return Imgprofi;
    }


}
