package ubb.postuniv.riddingaddict.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springfox.documentation.spring.web.json.Json;
import ubb.postuniv.riddingaddict.model.enums.AccessoryType;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ApiModel(description = "Details about the product")
public class ProductDTOResponse {

    private String productCode;
    private String name;
    private double price;
    private int quantity;
    private String shortDescription;
    private ProductCategory category;
    private LocalDate dateAdded;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BikeType bikeType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccessoryType accessoryType;

    private ProductDTOResponse(ProductDTOResponse.ProductDTOResponseBuilder builder) {
        this.productCode = builder.productCode;
        this.name = builder.name;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.shortDescription = builder.shortDescription;
        this.category = builder.category;
        this.dateAdded = builder.dateAdded;

        this.bikeType = builder.bikeType;
        this.accessoryType = builder.accessoryType;
    }

    public static class ProductDTOResponseBuilder {
        private final String productCode;
        private final String name;
        private final double price;
        private final int quantity;
        private final String shortDescription;
        private final ProductCategory category;
        private final LocalDate dateAdded;

        private BikeType bikeType;
        private AccessoryType accessoryType;


        public ProductDTOResponseBuilder(String productCode, String name, double price, int quantity, String shortDescription, ProductCategory category, LocalDate dateAdded) {
            this.productCode = productCode;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.shortDescription = shortDescription;
            this.category = category;
            this.dateAdded = dateAdded;
        }

        public ProductDTOResponse.ProductDTOResponseBuilder bikeType(BikeType bikeType) {

            this.bikeType = bikeType;
            return this;
        }

        public ProductDTOResponse.ProductDTOResponseBuilder accessoryType(AccessoryType accessoryType) {

            this.accessoryType = accessoryType;
            return this;
        }

        public ProductDTOResponse build() {

            ProductDTOResponse productDTOResponse = new ProductDTOResponse(this);
            return productDTOResponse;
        }
    }
}
