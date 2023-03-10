package query;

import entity.Catalog;
import entity.Order;
import entity.Product;
import entity.ShoppingCart;
import entity.User;
import entity.embeddable.PersonalInformation;
import entity.embedded.DeliveryAdress;
import entity.enums.Brand;
import entity.enums.Color;
import entity.enums.PaymentCondition;
import entity.enums.Role;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

@UtilityClass
public class DataImport {

    public void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();

        var smartphone = saveCatalog(session, "Smartphone");
        var tv = saveCatalog(session, "TV");

        var apple13 = saveProduct(session, smartphone, Brand.APPLE, "13", LocalDate.of(2022, 1, 10), 1000, Color.BLACK, "image");
        var apple14 = saveProduct(session, smartphone, Brand.APPLE, "14", LocalDate.of(2022, 9, 12), 1100, Color.WHITE, "image");
        var samsungS21 = saveProduct(session, smartphone, Brand.SAMSUNG, "S21", LocalDate.of(2022, 1, 10), 1050, Color.WHITE, "image");
//        var samsungS22 = saveProduct(session, smartphone, Brand.SAMSUNG, "S22", LocalDate.of(2022, 1, 12), 1100, Color.BLACK, "image");
        var samsungA80J = saveProduct(session, tv, Brand.SAMSUNG, "A80J", LocalDate.of(2022, 1, 12), 2000, Color.BLACK, "image");

        var ivan = saveUser(session, "Ivan", "Ivanov", "ivan@gmail.com", "ivan", "123-45-67", LocalDate.of(1990, 10, 10), Role.USER);
        var sveta = saveUser(session, "Sveta", "Svetikova", "sveta@gmail.com", "sveta", "123-67-47", LocalDate.of(1985, 2, 2), Role.USER);
        var petr = saveUser(session, "Petr", "Petrov", "petr@gmail.com", "petr", "321-45-67", LocalDate.of(1980, 3, 3), Role.USER);
        var admin = saveUser(session, "Admin", "Admin", "admin@gmail.com", "admin", "111-11-11", LocalDate.of(1992, 1, 1), Role.ADMIN);

        var orderIvan = saveOrder(session, "Minsk", "Masherova", 17, LocalDate.of(2023, 12, 10), PaymentCondition.CARD, ivan);
        var orderSveta = saveOrder(session, "Minsk", "Pobedi", 10, LocalDate.of(2023, 11, 5), PaymentCondition.CASH, sveta);
        var orderPetr = saveOrder(session, "Smorgon", "Minskaya", 11, LocalDate.of(2023, 10, 4), PaymentCondition.CARD, petr);
        var orderPetr1 = saveOrder(session, "Smorgon", "Minskaya", 11, LocalDate.of(2023, 10, 4), PaymentCondition.CARD, petr);
        var orderPetr2 = saveOrder(session, "Smorgon", "Minskaya", 11, LocalDate.of(2023, 10, 4), PaymentCondition.CARD, petr);

        addToShoppingCart(session, LocalDate.now(), orderIvan, apple13);
        addToShoppingCart(session, LocalDate.now(), orderSveta, apple14);
        addToShoppingCart(session, LocalDate.now(), orderPetr, samsungA80J);
//        addToShoppingCart(session, LocalDate.now(), orderPetr1, samsungS22);
        addToShoppingCart(session, LocalDate.now(), orderPetr2, samsungS21);
    }
    private void addToShoppingCart(Session session, LocalDate instant, Order order, Product... products){
        Arrays.stream(products)
                .map(product -> ShoppingCart.builder()
                        .order(order)
                        .product(product)
                        .createdAt(instant)
                        .build())
                .forEach(session::save);
    }


    private Order saveOrder(Session session,
                            String city,
                            String street,
                            Integer building,
                            LocalDate deliveryDate,
                            PaymentCondition paymentCondition,
                            User user
    ) {

        var order = Order.builder()
                .deliveryAdress(DeliveryAdress.builder()
                        .city(city)
                        .street(street)
                        .building(building)
                        .build())
                .deliveryDate(deliveryDate)
                .paymentCondition(paymentCondition)
                .user(user)
                .build();

        session.save(order);

        return order;
    }

    private User saveUser(Session session,
                          String firstName,
                          String lastName,
                          String email,
                          String password,
                          String telephone,
                          LocalDate birthDate,
                          Role role) {

        var user = User.builder()
                .personalInformation(PersonalInformation.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(password)
                        .telephone(telephone)
                        .birthDate(birthDate)
                        .build())
                .role(role)
                .build();

        session.save(user);

        return user;
    }

    private Product saveProduct(Session session,
                                Catalog catalog,
                                Brand brand,
                                String model,
                                LocalDate dateOfRelease,
                                Integer price,
                                Color color,
                                String image) {
        var product = Product.builder()
                .catalog(catalog)
                .brand(brand)
                .model(model)
                .dateOfRelease(dateOfRelease)
                .price(price)
                .color(color)
                .image(image)
                .build();

        session.save(product);

        return product;
    }



    private Catalog saveCatalog(Session session, String categoryName) {
        Catalog catalog = Catalog.builder()
                .category(categoryName)
                .build();
        session.save(catalog);

        return catalog;
    }
}