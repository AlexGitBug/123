import entity.Catalog;
import entity.enums.Brand;
import entity.enums.Color;
import query.ProductFilter;
import util.HibernateUtil;

import java.time.LocalDate;

public class TestRunner {

    public static void main(String[] args) {

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var smartphone = ProductFilter.builder()
                    .catalog(Catalog.builder()
                            .category("Smartphone")
                            .build())
                    .price(1000)
                    .model("13")
                    .brand(Brand.APPLE)
                    .dateOfRelease(LocalDate.of(2022, 1, 10))
                    .color(Color.BLACK)
                    .build();




//            var user = session.get(User.class, 1);
//            var product = session.get(Product.class, 1);
//            var product1 = session.get(Product.class, 2);
//
//            var order = Order.builder()
//                    .deliveryAdress(DeliveryAdress.builder()
//                            .city("Minsk")
//                            .street("Pobeda")
//                            .building(1)
//                            .build())
//                    .deliveryDate(LocalDate.of(2022, 2, 25))
//                    .paymentCondition(PaymentCondition.CASH)
//                    .build();
//            order.setUser(user);
//            session.save(order);
//
//            var shoppingCart = ShoppingCart.builder().build();
//            shoppingCart.addProduct(session, order, product, product1);


        }
    }
}

