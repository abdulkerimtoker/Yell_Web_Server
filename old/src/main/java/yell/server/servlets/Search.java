package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;
import org.json.JSONArray;
import org.json.JSONObject;
import yell.server.sql.SqlService;
import yell.server.type.User;
import yell.server.util.DateFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@WebServlet(name = "Search")
public class Search extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String keyword = request.getParameter("keyword");

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("FROM User AS u " +
                    "WHERE LOWER(u.username) LIKE LOWER(:keyword) " +
                    "OR " +
                    "LOWER(u.description) LIKE LOWER(:keyword)");

            query.setMaxResults(25);
            query.setParameter("keyword", '%' + keyword + '%');

            List<User> searchResult = query.list();

            session.close();

            JSONArray userArray = new JSONArray();

            for (User u : searchResult) {
                JSONObject user = new JSONObject();

                user.put("username", u.getUsername());
                user.put("description", u.getDescription());
                user.put("image_id", u.getImageId());
                user.put("sign_up_date", DateFormatter.format(u.getSignUpDate()));

                userArray.put(user);
            }

            out.print(userArray.toString());
        } catch (Exception e) {
            out.print("error");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
