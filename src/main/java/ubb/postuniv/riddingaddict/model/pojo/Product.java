package ubb.postuniv.riddingaddict.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Product extends BaseEntity<Long> {

    @Column(unique = true)
    private String productCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    private String shortDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(name = "date_added")
    private LocalDate dateAdded;


    public Product(String name, double price, int quantity, String shortDescription, ProductCategory category) {
        this.productCode = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
        this.category = category;
    }
    public Product(String name, double price, int quantity, String shortDescription) {
        this.productCode = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
    }
}
