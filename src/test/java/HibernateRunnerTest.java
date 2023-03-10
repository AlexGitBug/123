import entity.Catalog;
import entity.Order;
import entity.User;
import entity.embedded.DeliveryAdress;
import entity.enums.PaymentCondition;
import entity.enums.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
//import util.HibernateTestUtil;
import util.HibernateUtil;

import java.time.LocalDate;

class HibernateRunnerTest {

    @Test
    void checkManyToMany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Catalog tv = Catalog.builder()
                    .category("TV")
                    .build();
            session.save(tv);
//            session.get(User.class,1);


//            Product product = session.get(Product.class, 1);
//            Product product1 = session.get(Product.class, 2)
//            ;
////            Order order = session.get(Order.class, 1);
//
//            product.addOrder(order);
//            product1.addOrder(order);

            session.getTransaction().commit();
        }
    }

    private static User getUser() {
        User user = User.builder()
                .id(1)
//                .firstName("Test")
//                .lastName("Test")
//                .email("Test")
//                .password("Test")
//                .telephone("Test")
//                .birthDate(LocalDate.of(2020, 1, 1))
//                .role(Role.USER)
                .build();
        return user;
    }

    @Test
    void checkMany() {
        try (  SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
               Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            User user = session.get(User.class, 1);
            Order build = Order.builder()
                    .user(user)
                    .deliveryAdress(DeliveryAdress.builder()
                            .city("123")
                            .street("123")
                            .building(132)
                            .build())
                    .deliveryDate(LocalDate.of(2020, 2 , 1))
                    .paymentCondition(PaymentCondition.CASH)
                    .build();
            session.save(build);




            session.getTransaction().commit();
        }
    }

}