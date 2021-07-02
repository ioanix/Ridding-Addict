package ubb.postuniv.riddingaddict.mapper;

import org.springframework.stereotype.Component;
import ubb.postuniv.riddingaddict.model.dto.ProductDTORequest;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;
import ubb.postuniv.riddingaddict.model.pojo.Accessory;
import ubb.postuniv.riddingaddict.model.pojo.Bike;
import ubb.postuniv.riddingaddict.model.pojo.Product;
import ubb.postuniv.riddingaddict.model.pojo.ProductFactory;

@Component
public class ProductRequestMapper extends AbstractMapper<Product, ProductDTORequest> {

    @Override
    public Product convertDtoToModel(ProductDTORequest productDtoRequest) {

//        return ProductFactory.getProductRequest(productDtoRequest.getCategory(),
//                productDtoRequest.getName(),
//                productDtoRequest.getPrice(),
//                productDtoRequest.getQuantity(),
//                productDtoRequest.getShortDescription(),
//                productDtoRequest.getBikeType(),
//                productDtoRequest.getAccessoryType());
        return ProductFactory.getProductRequest(
                productDtoRequest.getName(),
                productDtoRequest.getPrice(),
                productDtoRequest.getQuantity(),
                productDtoRequest.getShortDescription(),
                productDtoRequest.getBikeType(),
                productDtoRequest.getAccessoryType());
    }

    @Override
    public ProductDTORequest convertModelToDto(Product product) {

        ProductDTORequest productDtoRequest = null;

        if (String.valueOf(ProductCategory.BIKE).equals(String.valueOf(product.getCategory()))) {

            Bike bike = (Bike) product;

            productDtoRequest = new ProductDTORequest.ProductDTORequestBuilder(bike.getName(), bike.getPrice(), bike.getQuantity(), bike.getShortDescription())
                    .bikeType(bike.getBikeType())
                    .build();
        }

        if (String.valueOf(ProductCategory.ACCESSORY).equals(String.valueOf(product.getCategory()))) {

            Accessory accessory = (Accessory) product;

            productDtoRequest = new ProductDTORequest.ProductDTORequestBuilder(accessory.getName(), accessory.getPrice(), accessory.getQuantity(), accessory.getShortDescription())
                    .accessoryType(accessory.getAccessoryType())
                    .build();
        }

        return productDtoRequest;
    }
}
