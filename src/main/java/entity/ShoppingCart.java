package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Arrays;


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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "created_at")
    private Instant createdAt;

    public void setOrder(Order order) {
        this.order = order;
        this.order.getShoppingCart().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        this.product.getShoppingCart().add(this);
    }

    public void addProduct(Session session, Order order, Product... products) {
        Arrays.stream(products)
                .map(product -> ShoppingCart.builder()
                        .order(order)
                        .product(product)
                        .createdAt(Instant.now())
                        .build())
                .forEach(session::save);
    }
}