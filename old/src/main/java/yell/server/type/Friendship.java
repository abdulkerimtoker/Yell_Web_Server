package yell.server.type;

import javax.persistence.*;

/**
 * Created by abdulkerim on 05.04.2016.
 */
@Entity
@Table(name = "FRIENDSHIP")
public class Friendship {
    private int id;
    private User user1;
    private User user2;

    public Friendship() {}

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
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
    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    @ManyToOne
    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}
