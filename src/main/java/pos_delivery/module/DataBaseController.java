package pos_delivery.module;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import pos_delivery.model.Customer;
import pos_delivery.model.Menu;
import pos_delivery.model.Order;

import java.sql.Date;
import java.util.List;

public final class DataBaseController {

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

    /***************************************************************************
     *                                                                         *
     * Customer                                                                *
     *                                                                         *
     **************************************************************************/

    /**
     * Store Customer in DB, Customer Includes List of Orders
     */
    public void saveOrder(Customer customer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(customer);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Retrieve List of Customer within Given Dates
     *
     * @param startDate Starting Date
     * @param endDate   Ending Date
     */
    public List<Customer> retrieveOrder(Date startDate, Date endDate) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Customer> customers = session
                .createQuery("FROM Customer WHERE DATE BETWEEN :startDate AND :endDate ", Customer.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
        session.getTransaction().commit();
        session.close();

        return customers;
    }

    /**
     * Retrieve List of Orders Associated with Customer
     *
     * @param customer Associated Customer
     */
    public List<Order> retrieveOrderDetail(Customer customer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Order> orders = session
                .createQuery("FROM Order WHERE CUSTOMER = :orderNumber", Order.class)
                .setParameter("orderNumber", customer.getOrderNumber())
                .list();
        session.getTransaction().commit();
        session.close();

        return orders;
    }

    /**
     * Delete Customer from DB
     */
    public void deleteOrder(Customer customer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(customer);
        session.getTransaction().commit();
        session.close();
    }

    /***************************************************************************
     *                                                                         *
     * Menu                                                                    *
     *                                                                         *
     **************************************************************************/

    /**
     * Store Menu in DB
     */
    void createMenu(Menu menu) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(menu);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Retrieve All Menus from DB
     */
    List<Menu> retrieveMenu() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Menu> result = session.createQuery("FROM Menu", Menu.class).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Update existing Menu
     */
    void updateMenu(Menu menu) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(menu);
        session.getTransaction().commit();
        session.close();
    }
}