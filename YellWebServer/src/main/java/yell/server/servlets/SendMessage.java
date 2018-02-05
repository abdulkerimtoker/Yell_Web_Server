package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;
import yell.server.sql.SqlService;
import yell.server.type.GcmSession;
import yell.server.type.Message;
import yell.server.type.User;
import yell.server.util.DateFormatter;
import yell.server.util.YellGcmProtocols;
import yell.server.xmpp.XmppService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by abdulkerim on 24.05.2016.
 */
@WebServlet(name = "SendMessage")
public class SendMessage extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("UTF-8");

        Date date = new Date();

        DataInputStream in = new DataInputStream(request.getInputStream());

        String requestString = in.readUTF();
        JSONObject requestJson = new JSONObject(requestString);

        String sessionKey  = requestJson.getString("session_key");
        String target      = requestJson.getString("target");
        String message     = requestJson.getString("message");
        String messageType = requestJson.getString("message_type");

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("SELECT d.user FROM DeviceSession AS d " +
                    "WHERE d.key = :key")
                    .setMaxResults(1)
                    .setParameter("key", sessionKey);

            List<User> sessionResult = query.list();

            if (!sessionResult.isEmpty()) {
                User sender = sessionResult.get(0);

                query = session.createQuery("FROM GcmSession AS g " +
                        "WHERE g.user.username = :target")
                        .setMaxResults(1)
                        .setParameter("target", target);

                List<GcmSession> gcmSessionResult = query.list();

                if (!gcmSessionResult.isEmpty()) {
                    JSONObject messageData = new JSONObject();

                    messageData.put("downstream_type", "receive_message");
                    messageData.put("message_text", message);
                    messageData.put("messagetype", messageType);
                    messageData.put("sender", sender.getUsername());
                    messageData.put("date", DateFormatter.format(date));

                    // Send the message to all devices of the target
                    for (GcmSession gcmSession : gcmSessionResult) {
                        String messageId = UUID.randomUUID().toString();

                        JSONObject messageJson = XmppService.createMessageJson(gcmSession.getGcmToken(),
                                messageId, messageData, null);

                        XmppService.getConnection().sendStanza(XmppService.createPacketFromJson(messageJson));
                    }
                } else {
                    query = session.createQuery("FROM User AS u " +
                            "WHERE u.username = :username")
                            .setMaxResults(1)
                            .setParameter("username", target);

                    User user = (User) query.list().get(0);

                    if (user != null) {
                        Message msg = new Message(sessionResult.get(0),
                                user,
                                message,
                                messageType,
                                date);

                        session.save(msg);
                    }
                }

                out.print("done");
            } else {
                out.write("wrong");
            }

            session.close();
        } catch (Exception e) {
            out.write("error");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
