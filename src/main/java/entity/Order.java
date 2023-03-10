package entity;

import entity.embedded.DeliveryAdress;
import entity.enums.PaymentCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@NamedEntityGraph(name = "findAllOrdersWithProductsOfOneUser",
        attributeNodes = {
                @NamedAttributeNode(value = "shoppingCarts", subgraph = "product"),
                @NamedAttributeNode(value = "user")
        }, subgraphs = {
        @NamedSubgraph(name = "product", attributeNodes = @NamedAttributeNode("product"))
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "shoppingCarts")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void setUser(User user) {
        this.user = user;
        this.user.getOrders().add(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<ShoppingCart> shoppingCarts = new ArrayList<>();


}