package yell.server.type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by abdulkerim on 04.04.2016.
 */
@Entity
@Table(name = "YELL_USER")
public class User {
    private int id;
    private String username;
    private String password;
    private String description;
    private String imageId;
    private Date signUpDate;

    public User() {}

    public User(String username, String password, String description, String imageId, Date signUpDate) {
        this.username = username;
        this.password = password;
        this.description = description;
        this.imageId = imageId;
        this.signUpDate = signUpDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "username", unique = true, nullable = false, length = 16)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, length = 16)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "description", nullable = false, length = 256)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_id", length = 48)
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Column(name = "sign_up_date", nullable = false)
    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }
}
