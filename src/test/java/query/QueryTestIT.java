package query;

import entity.Catalog;
import entity.Order;
import entity.Product;
import entity.embeddable.PersonalInformation;
import entity.embedded.DeliveryAdress;
import entity.enums.Brand;
import entity.enums.Color;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import query.filter.CatalogFilter;
import query.filter.OrderFilter;
import query.filter.ProductFilter;
import query.filter.UserFilter;
import util.DataImport;
import util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class QueryTestIT {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final Query query = Query.getInstance();

    @BeforeAll
    public void initDb() {
        DataImport.importData(sessionFactory);
    }

    @AfterAll
    public void finish() {
        sessionFactory.close();
    }


    @Test
    void findOneProductEq() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var productFilter = ProductFilter.builder()
                .catalog(Catalog.builder()
                        .category("Smartphone")
                        .build())
                .price(1000)
                .model("13")
                .brand(Brand.APPLE)
                .dateOfRelease(LocalDate.of(2022, 1, 10))
                .color(Color.BLACK)
                .build();

        var results = query.findOneProductEq(session, productFilter);
        assertThat(results).hasSize(1);

        var brands = results.stream().map(Product::getFullFilterForOneProduct).collect(toList());
        assertThat(brands).contains("Smartphone APPLE 13 2022-01-10 1000 BLACK");

        session.getTransaction().commit();
//        results.stream().map()

    }

    @Test
    void findProductsGtPriceAndBrandAndCategory() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var productFilter = ProductFilter.builder()
                .catalog(Catalog.builder()
                        .category("Smartphone")
                        .build())
                .price(1000)
                .brand(Brand.SAMSUNG)
                .build();

        var results = query.findProductsGtPriceAndBrand(session, productFilter);
        assertThat(results).hasSize(2);

        var brands = results.stream().map(Product::getBrandAndPrice).collect(toList());
        assertThat(brands).contains("SAMSUNG 1050", "SAMSUNG 1100");

        session.getTransaction().commit();
    }

    @Test
    void findAllProductOfBrand() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var productFilter = ProductFilter.builder()
                .brand(Brand.SAMSUNG)
                .build();

        var results = query.findAllProductOfBrand(session, productFilter);
        assertThat(results).hasSize(3);

        var brands = results.stream().map(Product::getBrand).toList();
        assertThat(brands).contains(productFilter.getBrand());

        session.getTransaction().commit();
    }

    @Test
    void findCatalogOfProductWithBrandFromShoppingCart() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        CatalogFilter catalogFilter = CatalogFilter.builder().category("Smartphone").build();
        ProductFilter productFilter = ProductFilter.builder().brand(Brand.SAMSUNG).build();

        List<Catalog> results = query.findCatalogOfProductWithBrandFromShoppingCart(session, catalogFilter, productFilter);
        assertThat(results).hasSize(1);

        List<String> actualResult = results.stream().map(Catalog::getCategory).collect(toList());
        assertThat(actualResult).contains("Smartphone");


        session.getTransaction().commit();
    }

    @Test
    void findAllProductsFromOrder() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        OrderFilter orderFilter = OrderFilter.builder()
                .deliveryAdress(DeliveryAdress.builder()
                        .city("Smorgon")
                        .street("Minskaya")
                        .building(11)
                        .build())
                .build();


        List<Product> results = query.findAllProductsFromOrder(session, orderFilter);
        assertThat(results).hasSize(3);

        List<String> actualResult = results.stream().map(Product::getModel).toList();
        assertThat(actualResult).containsExactlyInAnyOrder("A80J", "S22", "S21");

        session.getTransaction().commit();
    }

    @Test
    void findAllOrdersWithProductsOfOneUser() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        UserFilter userFilter = UserFilter.builder()
                .personalInformation(PersonalInformation.builder()
                        .email("petr@gmail.com")
                        .build())
                .build();

        List<Order> results = query.findAllOrdersWithProductsOfOneUser(session, userFilter);
        assertThat(results).hasSize(3);

        List<Integer> actualResult = results.stream().map(Order::getId).collect(toList());
        assertThat(actualResult).contains(3, 4);

        session.getTransaction().commit();
    }

    @Test
    void findUsersWhoMadeAnOrderAtASpecificTime() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        OrderFilter orderFilter = OrderFilter.builder()

                .build();

        session.getTransaction().commit();
    }

}