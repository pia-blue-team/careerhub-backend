package com.careerhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Document("users")
public class User {
    @Id
    private String id;

    private static int userCounter = 0;
    private int userId;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String cvPath;

    private String aboutUser;

    private String currentRole;

    private List<String> appliedJobIds = new ArrayList<>();


    public User(String name, String surname, String email, String password, String aboutUser, String currentRole, String cvPath) {
        userCounter++;
        this.userId = userCounter;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.aboutUser = aboutUser;
        this.currentRole = currentRole;
        this.cvPath = cvPath;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(String aboutUser) {
        this.aboutUser = aboutUser;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public List<String> getAppliedJobIds() {
        return appliedJobIds;
    }

    public void setAppliedJobIds(List<String> appliedJobIds) {
        this.appliedJobIds = appliedJobIds;
    }

    public User() {
    }
}