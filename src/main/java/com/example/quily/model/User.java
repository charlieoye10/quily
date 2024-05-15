package com.example.quily.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "User") // This is the table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "firstName", nullable = false, length = 30)
    private String firstName;

    @Column(name = "middleName", length = 30)
    private String middleName;

    @Column(name = "lastName", length = 30)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 30)
    private String email;

    @Column(name = "mobileNumber", unique = true, length = 30)
    private String mobileNumber;

    @Column(name = "accountCreationDate", nullable = false)
    private Date accountCreationDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "userType", length = 10)
    private String userType;

    public User() {}

    public User(String firstName, String middleName, String lastName, String email, String mobileNumber, Date accountCreationDate, boolean active, String userType) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.accountCreationDate = accountCreationDate;
        this.active = active;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", accountCreationDate=" + accountCreationDate +
                ", active=" + active +
                ", userType='" + userType + '\'' +
                '}';
    }
}
