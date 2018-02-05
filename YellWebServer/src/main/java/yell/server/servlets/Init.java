package yell.server.servlets;

import org.hibernate.Session;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import yell.server.gcm.GcmUtils;
import yell.server.sql.SqlService;
import yell.server.type.User;
import yell.server.xmpp.XmppService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by abdulkerim on 17.05.2016.
 */
@WebServlet(name = "Init")
public class Init extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (SqlService.getSessionFactory() == null) {
            SqlService.establishSessionFactory(getServletContext().getRealPath("/WEB-INF/hibernate.cfg.xml"));
        }

        if (XmppService.getConnection() == null) {
            new Thread(() -> {
                try {
                    XmppService.establishConnection()
                            .connect()
                            .login(GcmUtils.GCM_SENDER_ID + GcmUtils.GCM_API_SERVER, GcmUtils.GCM_API_KEY);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
