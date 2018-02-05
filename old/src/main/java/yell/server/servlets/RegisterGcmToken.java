package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;
import yell.server.sql.SqlService;
import yell.server.type.DeviceSession;
import yell.server.type.GcmSession;
import yell.server.type.Message;
import yell.server.type.User;
import yell.server.util.YellGcmProtocols;
import yell.server.xmpp.XmppService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

/**
 * Created by abdulkerim on 29.05.2016.
 */
@WebServlet(name = "RegisterGcmToken")
public class RegisterGcmToken extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String session_key = request.getParameter("session_key");
        String gcm_token   = request.getParameter("gcm_token");

        try {
            Session session = SqlService.getSessionFactory().openSession();

            // Look for a session matching the given session key
            Query query = session.createQuery("FROM DeviceSession AS d " +
                    "WHERE d.key = :session_key")
                    .setMaxResults(1)
                    .setParameter("session_key", session_key);

            List<DeviceSession> deviceSessionResult = query.list();

            // If the session key is valid, save a GCM Session for the requester
            if (!deviceSessionResult.isEmpty()) {
                User user = deviceSessionResult.get(0).getUser();

                // Delete all past GCM Sessions belonging to the user
                query = session.createQuery("DELETE FROM GcmSession AS g " +
                        "WHERE g.user = :user")
                        .setParameter("user", user);

                query.executeUpdate();

                // Save a GCM Session
                GcmSession newGcmSession = new GcmSession(gcm_token, user);
                session.save(newGcmSession);

                new Thread(() -> {
                    sendUnsentMessages(newGcmSession);
                }).start();

                out.print("done");
            } else {
                out.print("wrong");
            }

            session.close();
        } catch (Exception e) {
            out.print("error");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void sendUnsentMessages(GcmSession gcmSession) {
        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("FROM Message AS m " +
                    "WHERE m.receiver = :receiver")
                    .setParameter("receiver", gcmSession.getUser());

            List<Message> messageResult = query.list();

            for (Message message : messageResult) {
                JSONObject data = new JSONObject();

                data.put("downstream_type", YellGcmProtocols.YELL_RECEIVE_MESSAGE);
                data.put("sender", message.getSender().getUsername());
                data.put("message_text", message.getMessage());
                data.put("message_type", message.getMesageType());
                data.put("date", message.getDate());

                JSONObject gcmData = XmppService.createMessageJson(gcmSession.getGcmToken(),
                        UUID.randomUUID().toString(), data, null);

                try {
                    XmppService.getConnection().sendStanza(XmppService.createPacketFromJson(gcmData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
