package ubb.postuniv.riddingaddict.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ubb.postuniv.riddingaddict.model.enums.AccessoryType;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ProductDTO {

    private String name;
    private double price;
    private int quantity;
    private String shortDescription;
    private ProductCategory category;

    private BikeType bikeType;
    private AccessoryType accessoryType;

    private ProductDTO(ProductDTOBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.shortDescription = builder.shortDescription;
        this.category = builder.category;

        this.bikeType = builder.bikeType;
        this.accessoryType = builder.accessoryType;
    }

    public static class ProductDTOBuilder {

        private final String name;
        private final double price;
        private final int quantity;
        private final String shortDescription;
        private final ProductCategory category;

        private BikeType bikeType;
        private AccessoryType accessoryType;


        public ProductDTOBuilder(String name, double price, int quantity, String shortDescription, ProductCategory category) {

            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.shortDescription = shortDescription;
            this.category = category;
        }

        public ProductDTOBuilder bikeType(BikeType bikeType) {

            this.bikeType = bikeType;
            return this;
        }

        public ProductDTOBuilder accessoryType(AccessoryType accessoryType) {

            this.accessoryType = accessoryType;
            return this;
        }

        public ProductDTO build() {

            ProductDTO productDTO = new ProductDTO(this);
            return productDTO;
        }
    }
}
