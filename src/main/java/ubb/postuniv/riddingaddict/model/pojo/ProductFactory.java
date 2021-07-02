package ubb.postuniv.riddingaddict.model.pojo;


import ubb.postuniv.riddingaddict.exception.ItemNotFoundException;
import ubb.postuniv.riddingaddict.model.enums.AccessoryType;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;

import java.time.LocalDate;

public class ProductFactory {

    private ProductFactory() {
    }

    public static Product getProductRequest(String name, double price, int quantity, String shortDescription, BikeType bikeType, AccessoryType accessoryType) {

        if (accessoryType == null) {
            Bike bike = new Bike(name, price, quantity, shortDescription, bikeType);
            bike.setCategory(ProductCategory.BIKE);

            return bike;

        } else if (bikeType == null) {
            Accessory accessory = new Accessory(name, price, quantity, shortDescription, accessoryType);
            accessory.setCategory(ProductCategory.ACCESSORY);

            return accessory;

        } else {
            throw new ItemNotFoundException("Product category not found");
        }
    }

        //    public static Product getProductRequest(ProductCategory category, String name, double price, int quantity, String shortDescription, BikeType bikeType, AccessoryType accessoryType) {
//
//        if(category == null) {
//
//            throw new ShopException("You must provide the category");
//        }
//        switch (category) {
//
//            case BIKE:
//                return new Bike(name, price, quantity, shortDescription, category, bikeType);
//
//            case ACCESSORY:
//                return new Accessory(name, price, quantity, shortDescription, category, accessoryType);
//
//            default:
//                throw new ItemNotFoundException("Product category not found");
//        }
//    }

    public static Product getProductResponse(String productCode, ProductCategory category, String name, double price, int quantity, String shortDescription, LocalDate dateAdded, BikeType bikeType, AccessoryType accessoryType) {

        switch (category) {

            case BIKE:
                return new Bike(productCode, name, price, quantity, shortDescription, category, dateAdded, bikeType);

            case ACCESSORY:
                return new Accessory(productCode, name, price, quantity, shortDescription, category, dateAdded, accessoryType);

            default:
                throw new ItemNotFoundException("Product category not found");
        }
    }
}
