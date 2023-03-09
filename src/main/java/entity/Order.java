package entity;

import entity.embeddable.DeliveryAdress;
import entity.enums.PaymentCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "shoppingCart")
@Builder
@Entity
@Table(name = "orders", schema = "public")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private DeliveryAdress deliveryAdress;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "payment_condition")
    @Enumerated(EnumType.STRING)
    private PaymentCondition paymentCondition;

    @ManyToOne
    private User user;

    public void setUser(User user) {
        this.user = user;
        this.user.getOrders().add(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<ShoppingCart> shoppingCart = new ArrayList<>();

//    public void addProduct(Product product) {
//        ShoppingCart sc = new ShoppingCart();
//        sc.setOrder(this);
//        sc.setProduct(product);
//        sc.setCreatedAt(Instant.now());
//        shoppingCart.add(sc);
//    }


//    }
}