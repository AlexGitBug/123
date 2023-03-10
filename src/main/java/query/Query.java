package query;

import com.querydsl.jpa.impl.JPAQuery;
import entity.*;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import query.filter.*;
import service.QPredicate;

import java.util.List;

import static entity.QCatalog.catalog;
import static entity.QOrder.*;
import static entity.QOrder.order;
import static entity.QProduct.product;
import static entity.QShoppingCart.shoppingCart;
import static entity.QUser.user;


public class Query {

    private static final Query INSTANCE = new Query();

    //пробный тест на поиск определенного товара
    public List<Product> findOneProductEq(Session session, ProductFilter filter) {

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
                .join(product.catalog, catalog).fetchJoin()
                .where(predicate)
                .fetch();
    }

    //Список всех товаров выше 1000 и определенного бренда
    public List<Product> findProductsGtPriceAndBrand(Session session, ProductFilter filter) {

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
                .orderBy(product.price.asc())
                .fetch();
    }

    //Список всех продуктов по бренду
    public List<Product> findAllProductOfBrand(Session session, ProductFilter filter) {

        var predicate = QPredicate.builder()
                .add(filter.getBrand(), product.brand::eq)
                .buildAnd();

        return new JPAQuery<Product>(session)
                .select(product)
                .from(product)
                .where(predicate)
                .fetch();
    }

    public List<Catalog> findCatalogOfProductWithBrandFromShoppingCart(Session session, CatalogFilter catalogFilter, ProductFilter productFilter) {
        var predicate = QPredicate.builder()
                .add(productFilter.getBrand(), product.brand::eq)
                .add(catalogFilter.getCategory(), catalog.category::eq)
                .buildAnd();

        return new JPAQuery<Catalog>(session)
                .select(catalog)
                .from(catalog)
                .join(catalog.products, product)
                .join(product.shoppingCarts, shoppingCart)
                .where(predicate)
                .fetch();
    }

    public List<Product>  findAllProductsFromOrder(Session session, OrderFilter orderFilter) {
        var predicate = QPredicate.builder()
                .add(orderFilter.getDeliveryAdress().getCity(), order.deliveryAdress.city::eq)
                .add(orderFilter.getDeliveryAdress().getStreet(), order.deliveryAdress.street::eq)
                .add(orderFilter.getDeliveryAdress().getBuilding(), order.deliveryAdress.building::eq)
                .buildAnd();

        return new JPAQuery<Product>(session)
                .select(product)
                .from(product)
                .join(product.shoppingCarts, shoppingCart)
                .join(shoppingCart.order, order)
                .where(predicate)
                .fetch();
    }

    public List<Order> findAllOrdersWithProductsOfOneUser(Session session, UserFilter userFilter) {

        var predicate = QPredicate.builder()
                .add(userFilter.getPersonalInformation().getEmail(), user.personalInformation.email::eq)
                .buildAnd();

        return new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .join(order.user, user)
                .join(order.shoppingCarts, shoppingCart)
                .join(shoppingCart.product, product)
                .join(product.catalog, catalog)
//                .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("findAllOrdersWithProductsOfOneUser"))
                .where(predicate)
                .fetch();
    }
//
//    public List<User> findUsersWhoMadeAnOrderAtAMonth(Session session, ShoppingCartFilter shoppingCartFilter) {
//
//        var predicate = QPredicate.builder()
//                .add(shoppingCartFilter.getCreated)
//                .buildOr();
//
//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .join(user.orders, order)
//                .fetch();
//    }

    public List<User> findUsersWhoMadeAnOrderSpecificProduct(Session session, UserFilter userFilter) {
        return null;
    }

    public List<User> findUsersWhoPayOrderWithCash(Session session, UserFilter userFilter) {
        return null;
    }


    public static Query getInstance() {
        return INSTANCE;
    }


}