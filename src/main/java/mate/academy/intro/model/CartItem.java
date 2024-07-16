package mate.academy.intro.model;

import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ShoppingCart shoppingCart;
    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
    @Column(nullable = false)
    private int quantity;
}
