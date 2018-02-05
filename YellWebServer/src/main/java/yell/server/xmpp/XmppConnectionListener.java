package yell.server.xmpp;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by abdulkerim on 04.04.2016.
 */
public class XmppConnectionListener implements ConnectionListener {

    private FileWriter writer;

    public XmppConnectionListener() {
        try {
            writer = new FileWriter("C:\\Users\\Abdulkerim\\Desktop\\xmpp-log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connected(XMPPConnection connection) {
        try {
            writer.write("Connected to GCM XMPP Server");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authenticated(XMPPConnection connection, boolean resumed) {
        try {
            writer.write("Authenticated on GCM XMPP Server");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectionClosed() {
        try {
            writer.write("Connection closed");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectionClosedOnError(Exception e) {
        try {
            writer.write("Connection closed on error");
            writer.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        e.printStackTrace();
    }

    public void reconnectionSuccessful() {
        try {
            writer.write("Reconnected to GCM XMPP Server");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnectingIn(int seconds) {
        try {
            writer.write("Reconnecting to GCM XMPP Server in " + seconds + " secs");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnectionFailed(Exception e) {
        try {
            writer.write("Reconnection failed on error");
            writer.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        e.printStackTrace();
    }
}
