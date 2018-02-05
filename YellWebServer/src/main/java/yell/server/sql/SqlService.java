package yell.server.sql;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.File;

/**
 * Created by abdulkerim on 05.04.2016.
 */
public class SqlService {

    private static SessionFactory sessionFactory = null;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static SessionFactory establishSessionFactory(String hibernateConfFilePath) {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure(new File(hibernateConfFilePath))
                .build();

        sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();

        return sessionFactory;
    }
}
