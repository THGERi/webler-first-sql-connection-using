package hu.webler.entity;

import org.mindrot.jbcrypt.BCrypt;

public class User {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User() {
    }

    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Hash a password using BCrypt

    /*
    The workFactor in BCrypt is a measure of how much computational effort is used to hash the password. It's also known
    as the "cost factor" or "log rounds".  When you hash a password with BCrypt, the algorithm performs a certain number
    of iterations to generate the hash. The number of iterations is a power of 2, and it's determined by the workFactor.
    For example, if the workFactor is 10 (the default), BCrypt will perform 2^10 = 1024 iterations. If the workFactor is
    12, it will perform 2^12 = 4096 iterations.  Increasing the workFactor makes the hashing process more CPU-intensive
    and slower. This can help protect against brute-force attacks, because it makes each password guess more expensive
    for the attacker. However, it also makes legitimate logins slower, so there's a trade-off between security and
    performance.  It's also worth noting that as computers get faster, you might need to increase the workFactor to
    maintain the same level of security. BCrypt allows you to do this without re-hashing all your passwords: you can
    simply increase the workFactor for new passwords and when users next log in.
    */
    public String hashPassword(String password) {
        int workFactor = 12; // Increase the work factor to 12
        return BCrypt.hashpw(password, BCrypt.gensalt(workFactor));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
