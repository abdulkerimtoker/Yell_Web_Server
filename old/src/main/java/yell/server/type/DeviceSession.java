package yell.server.type;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by abdulkerim on 24.05.2016.
 */
@Entity
@Table(name = "DEVICE_SESSION")
public class DeviceSession {
    private int id;
    private User user;
    private String key;

    public DeviceSession() {}

    public DeviceSession(String key, User user) {
        this.key = key;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "key", length = 36, nullable = false, unique = true)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
