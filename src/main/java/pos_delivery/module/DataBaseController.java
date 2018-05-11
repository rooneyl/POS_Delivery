package pos_delivery.module;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DataBaseController {
    private static DataBaseController dataBaseController = null;
    private StandardServiceRegistry registry;
    private SessionFactory sessionFactory;

    private DataBaseController() {
    }

    public static DataBaseController getInstance() {
        if (dataBaseController == null)
            dataBaseController = new DataBaseController();
        return dataBaseController;
    }

    public void startDB() throws Exception {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                throw new Exception("Database Initialization Failed");
            }
        }
    }

    public void closeDataBase() {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(registry);
        dataBaseController = null;
    }
}
