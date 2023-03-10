package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "created_at")
    private LocalDate createdAt;

    public void setOrder(Order order) {
        this.order = order;
        this.order.getShoppingCarts().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        this.product.getShoppingCarts().add(this);
    }

    public void addProduct(Session session, Order order, Product... products) {
        Arrays.stream(products)
                .map(product -> ShoppingCart.builder()
                        .order(order)
                        .product(product)
                        .createdAt(LocalDate.now())
                        .build())
                .forEach(session::save);
    }

    public String getIdAndCatalogOfProduct() {
        return order.getId() + " " + product.getCatalog();
    }
}