package yell.server.type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by abdulkerim on 05.04.2016.
 */
@Entity
@Table(name = "MESSAGE")
public class Message {
    private int id;
    private User sender;
    private User receiver;
    private String message;
    private String mesageType;
    private Date date;

    public Message() {}

    public Message(User sender, User receiver, String message, String mesageType, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.mesageType = mesageType;
        this.date = date;
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
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @ManyToOne
    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    @Column(name = "message", length = 256, nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "message_type", length = 4, nullable = false)
    public String getMesageType() {
        return mesageType;
    }

    public void setMesageType(String mesageType) {
        this.mesageType = mesageType;
    }

    @Column(name = "message_date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
