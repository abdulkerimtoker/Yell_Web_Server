package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import yell.server.sql.SqlService;
import yell.server.type.User;
import yell.server.util.DateFormatter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdulkerim on 17.05.2016.
 */
@WebServlet(name = "Profiles")
public class Profiles extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String out;

        DataInputStream in = new DataInputStream(request.getInputStream());

        String requestString = in.readUTF();

        JSONArray usernamesJson = new JSONArray(requestString);

        List<String> usernames = new ArrayList<>();

        for (Object o : usernamesJson) {
            usernames.add((String) o);
        }

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("FROM User AS u " +
                    "WHERE u.username IN (:usernames)")
                    .setMaxResults(usernames.size())
                    .setParameterList("usernames", usernames);

            List<User> userList = query.list();

            session.close();

            JSONObject userResult = new JSONObject();

            for (User user : userList) {
                JSONObject json = new JSONObject();

                json.put("description", user.getDescription());
                json.put("image_id", user.getImageId());
                json.put("sign_up_date", DateFormatter.format(user.getSignUpDate()));

                userResult.put(user.getUsername(), json);
            }

            out = userResult.toString();
        } catch (Exception e) {
            out = "error";
            e.printStackTrace();
        }

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(out.getBytes(StandardCharsets.UTF_8));
        servletOutputStream.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
