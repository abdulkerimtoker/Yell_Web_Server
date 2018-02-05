package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import yell.server.sql.SqlService;
import yell.server.type.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by abdulkerim on 23.05.2016.
 */
@WebServlet(name = "ChangeDescription")
public class ChangeDescription extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionKey  = request.getParameter("session_key");
        String description = request.getParameter("description");

        PrintWriter out = response.getWriter();

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("SELECT u.user FROM DeviceSession AS u " +
                    "WHERE u.key = :key")
                    .setMaxResults(1)
                    .setParameter("key", sessionKey);

            List<User> userResult = query.list();

            if (!userResult.isEmpty()) {
                User user = userResult.get(0);

                query = session.createQuery("UPDATE User AS u SET u.description = :description " +
                        "WHERE u.username = :username")
                        .setMaxResults(1)
                        .setParameter("username", user.getUsername())
                        .setParameter("description", description);

                query.executeUpdate();

                out.print("done");
            } else {
                out.print("wrong");
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
