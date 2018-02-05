package yell.server.type;

import javax.persistence.*;

/**
 * Created by abdulkerim on 05.04.2016.
 */
@Entity
@Table(name = "BLOCKING")
public class Blocking {
    private int id;
    private User blocker;
    private User blocked;

    public Blocking() {}

    public Blocking(User blocked, User blocker) {
        this.blocked = blocked;
        this.blocker = blocker;
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
    public User getBlocked() {
        return blocked;
    }

    public void setBlocked(User blocked) {
        this.blocked = blocked;
    }

    @ManyToOne
    public User getBlocker() {
        return blocker;
    }

    public void setBlocker(User blocker) {
        this.blocker = blocker;
    }
}
