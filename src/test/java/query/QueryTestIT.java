package query;

import entity.Catalog;
import entity.Product;
import entity.enums.Brand;
import entity.enums.Color;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import util.HibernateUtil;

import java.time.LocalDate;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class QueryTestIT {

    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final Query query = Query.getInstance();

    @BeforeAll
    public void initDb() {
        DataImporter.importData(sessionFactory);
    }

    @AfterAll
    public void finish() {
        sessionFactory.close();
    }


    @Test
    void findAllProducts() {
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

        var results = query.findAllProductsEq(session, productFilter);
        assertThat(results).hasSize(1);

        var brands = results.stream().map(Product::getBrand).collect(toList());
        assertThat(brands).containsExactlyInAnyOrder(productFilter.getBrand());

        session.getTransaction().commit();
//        results.stream().map()

    }

    @Test
    void findProductsGt1000AndSamsung() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var productFilter = ProductFilter.builder()
                .catalog(Catalog.builder()
                        .category("Smartphone")
                        .build())
                .price(1000)
                .brand(Brand.SAMSUNG)
                .build();

        var results = query.findProductsGt1000AndSamsung(session, productFilter);
        assertThat(results).hasSize(2);

        var brands = results.stream().map(Product::getBrand).collect(toList());
        assertThat(brands).contains(productFilter.getBrand());


        session.getTransaction().commit();
    }


//    @Test
//    void findAllByCompanyName() {
//        @Cleanup Session session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        List<User> results = userDao.findAllByCompanyName(session, "Google");
//        assertThat(results).hasSize(2);
//
//        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
//        assertThat(fullNames).containsExactlyInAnyOrder("Sergey Brin", "Diane Greene");
//
//        session.getTransaction().commit();
//    }

}