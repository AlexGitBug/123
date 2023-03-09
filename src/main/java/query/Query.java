package query;

import com.querydsl.jpa.impl.JPAQuery;
import entity.Product;
import org.hibernate.Session;
import service.QPredicate;

import java.util.List;

import static entity.QCatalog.catalog;
import static entity.QProduct.product;


public class Query {

    private static final Query INSTANCE = new Query();

    public List<Product> findAllProductsEq(Session session, ProductFilter filter) {

        var predicate = QPredicate.builder()
                .add(filter.getCatalog().getCategory(), catalog.category::eq)
                .add(filter.getBrand(), product.brand::eq)
                .add(filter.getDateOfRelease(), product.dateOfRelease::eq)
                .add(filter.getPrice(), product.price::eq)
                .add(filter.getColor(), product.color::eq)
                .buildAnd();

        return new JPAQuery<Product>(session)
                .select(product)
                .from(product)
                .join(product.catalog, catalog)
                .where(predicate)
                .fetch();
    }

    public List<Product> findProductsGt1000AndSamsung(Session session, ProductFilter filter) {

        var predicate = QPredicate.builder()
                .add(filter.getCatalog().getCategory(), catalog.category::eq)
                .add(filter.getBrand(), product.brand::eq)
                .add(filter.getPrice(), product.price::gt)
                .buildAnd();

        return new JPAQuery<Product>(session)
                .select(product)
                .from(product)
                .join(product.catalog, catalog)
                .where(predicate)
                .fetch();
    }



    public static Query getInstance() {
        return INSTANCE;
    }

}

//}
//    Brand brand;
//    String model;
//    LocalDate dateOfRelease;
//    Integer price;
//    Color color;