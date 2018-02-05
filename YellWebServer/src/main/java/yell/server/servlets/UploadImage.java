package yell.server.servlets;

import org.hibernate.Query;
import org.hibernate.Session;
import yell.server.sql.SqlService;
import yell.server.type.DeviceSession;
import yell.server.type.User;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by abdulkerim on 17.05.2016.
 */
@WebServlet(name = "UploadImage")
public class UploadImage extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String sessionKey = request.getHeader("session_key");
        String abc = request.getHeader("abc");
        String fileName = UUID.randomUUID().toString() + "." + abc;

        try {
            Session session = SqlService.getSessionFactory().openSession();

            Query query = session.createQuery("FROM DeviceSession AS d " +
                    "WHERE d.key = :session_key")
                    .setMaxResults(1)
                    .setParameter("session_key", sessionKey);

            List<DeviceSession> deviceSessionsResult = query.list();

            if (!deviceSessionsResult.isEmpty()) {
                User user = deviceSessionsResult.get(0).getUser();

                try {
                    File imageFolder = new File(getServletContext().getRealPath("/Images"));
                    imageFolder.mkdirs();

                    File file = new File(imageFolder, fileName);
                    file.createNewFile();

                    ServletInputStream input = request.getInputStream();
                    FileOutputStream fout = new FileOutputStream(file);

                    int len;
                    byte[] buffer = new byte[4096];
                    while ((len = input.read(buffer)) != -1) {
                        fout.write(buffer, 0, len);
                        fout.flush();
                    }

                    fout.close();
                    input.close();

                    out.write(fileName);

                    query = session.createQuery("UPDATE User AS u SET u.imageId = :image_id " +
                            "WHERE u = :muser")
                            .setMaxResults(1)
                            .setParameter("muser", user)
                            .setParameter("image_id", fileName);

                    query.executeUpdate();
                } catch (Exception e) {
                    out.write("error");
                    e.printStackTrace();
                }
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
