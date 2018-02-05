package yell.server.xmpp;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONObject;
import yell.server.sql.SqlService;
import yell.server.type.GcmSession;
import yell.server.type.User;
import yell.server.util.YellGcmProtocols;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by abdulkerim on 04.04.2016.
 */
public class XmppStanzaListener implements StanzaListener {

    private FileWriter writer;

    public XmppStanzaListener() {
        try {
            writer = new FileWriter("C:\\Users\\Abdulkerim\\Desktop\\xmpp-stanza-log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        GcmExtension gcmExtension = (GcmExtension) packet.getExtension(GcmExtension.GCM_ELEMENT_NAME, GcmExtension.GCM_NAMESPACE);

        JSONObject json = new JSONObject(gcmExtension.getJson());

        if (json.has("category")) {
            if (json.getString("category").equals("yell.client")) {
                JSONObject data = json.getJSONObject("data");
                String from = json.getString("from");

                switch (data.getInt("upstream_type")) {
                    case YellGcmProtocols.YELL_UPDATE_GCM_TOKEN:
                        updateGcmToken(from, data);
                }
            }
        }

        System.out.println(json);
    }

    public void updateGcmToken(String from, JSONObject data) {
        try {
            Session session = SqlService.getSessionFactory().openSession();

            String sessionKey = data.getString("session_key");

            Query query = session.createQuery("SELECT d.user FROM DeviceSession AS d " +
                    "WHERE d.key = :session_key")
                    .setMaxResults(1)
                    .setParameter("session_key", sessionKey);

            List<User> sessionResult = query.list();

            if (!sessionResult.isEmpty()) {
                User user = sessionResult.get(0);

                query = session.createQuery("DELETE FROM GcmSession AS g " +
                        "WHERE g.user = :user")
                        .setParameter("user", user);

                GcmSession newGcmSession = new GcmSession(from, user);

                session.save(newGcmSession);
            }

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
