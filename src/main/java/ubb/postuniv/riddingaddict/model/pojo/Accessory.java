package ubb.postuniv.riddingaddict.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ubb.postuniv.riddingaddict.model.enums.AccessoryType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Accessory extends Product {

    @Enumerated(EnumType.STRING)
    private AccessoryType accessoryType;

    public Accessory(String name, double price, int quantity, String shortDescription, ProductCategory category, AccessoryType accessoryType) {
        super(name, price, quantity, shortDescription, category);
        this.accessoryType = accessoryType;
    }

    public Accessory(String productCode, String name, double price, int quantity, String shortDescription, ProductCategory category, LocalDate dateAdded, AccessoryType accessoryType) {
        super(productCode, name, price, quantity, shortDescription, category, dateAdded);
        this.accessoryType = accessoryType;
    }

    public Accessory(String name, double price, int quantity, String shortDescription, AccessoryType accessoryType) {
        super(name, price, quantity, shortDescription);
        this.accessoryType = accessoryType;
    }
}
