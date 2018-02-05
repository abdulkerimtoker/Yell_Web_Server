package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import yell.server.sql.SqlService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Abdulkerim on 30.05.2016.
 */
@WebServlet(name = "Logout")
public class Logout extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String sessionKey = request.getParameter("session_key");

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("DELETE FROM DeviceSession AS d " +
                    "WHERE d.key = :session_key")
                    .setMaxResults(1)
                    .setParameter("session_key", sessionKey);

            query.executeUpdate();

            out.print("done");

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
