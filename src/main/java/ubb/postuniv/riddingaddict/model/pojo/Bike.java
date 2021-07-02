package ubb.postuniv.riddingaddict.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bike extends Product {

    @Enumerated(EnumType.STRING)
    private BikeType bikeType;

    public Bike(String name, double price, int quantity, String shortDescription, ProductCategory category, BikeType bikeType) {
        super(name, price, quantity, shortDescription, category);
        this.bikeType = bikeType;
    }

    public Bike(String productCode, String name, double price, int quantity, String shortDescription, ProductCategory category, LocalDate dateAdded, BikeType bikeType) {
        super(productCode, name, price, quantity, shortDescription, category, dateAdded);
        this.bikeType = bikeType;
    }

    public Bike(String name, double price, int quantity, String shortDescription, BikeType bikeType) {
        super(name, price, quantity, shortDescription);
        this.bikeType = bikeType;
    }
}
