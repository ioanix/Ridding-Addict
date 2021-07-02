package ubb.postuniv.riddingaddict.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ubb.postuniv.riddingaddict.model.enums.AccessoryType;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ApiModel(description = "Details about the product")
public class ProductDTORequest {

    private String name;
    private double price;
    private int quantity;
    private String shortDescription;
    //private ProductCategory category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BikeType bikeType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccessoryType accessoryType;

    private ProductDTORequest(ProductDTORequestBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.shortDescription = builder.shortDescription;
        //this.category = builder.category;

        this.bikeType = builder.bikeType;
        this.accessoryType = builder.accessoryType;
    }

    public static class ProductDTORequestBuilder {

        private final String name;
        private final double price;
        private final int quantity;
        private final String shortDescription;
        //private final ProductCategory category;

        private BikeType bikeType;
        private AccessoryType accessoryType;


        //        public ProductDTORequestBuilder(String name, double price, int quantity, String shortDescription, ProductCategory category) {
//            this.name = name;
//            this.price = price;
//            this.quantity = quantity;
//            this.shortDescription = shortDescription;
//            this.category = category;
//        }
        public ProductDTORequestBuilder(String name, double price, int quantity, String shortDescription) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.shortDescription = shortDescription;
            //this.category = category;
        }

        public ProductDTORequestBuilder bikeType(BikeType bikeType) {

            this.bikeType = bikeType;
            return this;
        }

        public ProductDTORequestBuilder accessoryType(AccessoryType accessoryType) {

            this.accessoryType = accessoryType;
            return this;
        }

        public ProductDTORequest build() {

            ProductDTORequest productDTORequest = new ProductDTORequest(this);
            return productDTORequest;
        }
    }
}
