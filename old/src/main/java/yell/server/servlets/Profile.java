package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;
import yell.server.sql.SqlService;
import yell.server.util.DateFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by abdulkerim on 17.05.2016.
 */
@WebServlet(name = "Profile")
public class Profile extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("FROM User AS u " +
                    "WHERE u.username = :username");
            query.setMaxResults(1);
            query.setParameter("username", username);

            List<yell.server.type.User> result = query.list();

            session.close();

            if (!result.isEmpty()) {
                yell.server.type.User user = result.get(0);

                JSONObject json = new JSONObject();

                json.put("username", user.getUsername());
                json.put("description", user.getDescription());
                json.put("image_id", user.getImageId());
                json.put("sign_up_date", DateFormatter.format(user.getSignUpDate()));

                out.print(json.toString());
            } else {
                out.print("empty");
            }
        } catch (Exception e) {
            out.print("error");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
