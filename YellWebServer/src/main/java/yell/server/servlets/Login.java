package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import yell.server.sql.SqlService;
import yell.server.type.DeviceSession;
import yell.server.type.User;

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
 * Created by abdulkerim on 24.05.2016.
 */
@WebServlet(name = "Login")
public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("FROM User AS u " +
                    "WHERE u.username = :username AND u.password = :password")
                    .setMaxResults(1)
                    .setParameter("username", username)
                    .setParameter("password", password);

            List<User> userResult = query.list();

            if (!userResult.isEmpty()) {
                User user = userResult.get(0);
                String key = UUID.randomUUID().toString();

                session.save(new DeviceSession(key, user));

                out.print(key);
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
}
