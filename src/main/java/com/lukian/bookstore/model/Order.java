package com.lukian.bookstore.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    /**
     * Note:
     * We have "Many" side (in bidirectional @OneToMany) that controls mapping, but in our case,
     * mapping is needed to be performed from the "One" side, thus we explicitly declare it.
     * Also {@code CascadeType.ALL} is specified
     * for order to delete items when being deleted itself
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @Column(name = "total", nullable = false)
    private BigDecimal total;
    @Column(name = "status",
            nullable = false,
            columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        COMPLETED,
        PENDING,
        DELIVERED,
        CANCELED,
        POSTPONED
    }
}
