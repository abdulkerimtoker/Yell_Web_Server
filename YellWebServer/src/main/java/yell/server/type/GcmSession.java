package yell.server.type;

import javax.persistence.*;

/**
 * Created by abdulkerim on 05.04.2016.
 */
@Entity
@Table(name = "GCM_SESSION")
public class GcmSession {
    private int id;
    private User user;
    private String gcmToken;

    public GcmSession() {}

    public GcmSession(String gcmToken, User user) {
        this.gcmToken = gcmToken;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "gcm_token", nullable = false, unique = true)
    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }
}
