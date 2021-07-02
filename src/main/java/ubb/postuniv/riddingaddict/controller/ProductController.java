package ubb.postuniv.riddingaddict.controller;

import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubb.postuniv.riddingaddict.mapper.Mapper;
import ubb.postuniv.riddingaddict.model.dto.ProductDTORequest;
import ubb.postuniv.riddingaddict.model.dto.ProductDTOResponse;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;
import ubb.postuniv.riddingaddict.model.pojo.Product;
import ubb.postuniv.riddingaddict.model.pojo.ProductCodeAndNameViewModel;
import ubb.postuniv.riddingaddict.model.validator.Validator;
import ubb.postuniv.riddingaddict.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Log4j2
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Mapper<Product, ProductDTORequest> productRequestMapper;

    @Autowired
    private Mapper<Product, ProductDTOResponse> productResponseMapper;

    @Autowired
    @Qualifier("productCategoryValidator")
    private Validator<String> categoryValidator;


    @GetMapping("/products")
    public ResponseEntity<List<ProductDTOResponse>> showAllProducts() {

        List<Product> products = productService.getAll();
        log.info("getAllProducts = {}", products);

        return new ResponseEntity<>(productResponseMapper.convertModelsToDtos(products), HttpStatus.OK);
    }

    @GetMapping("/products/descByPrice")
    public ResponseEntity<List<ProductDTOResponse>> showProductsOrderedByPriceDesc() {

        List<Product> products = productService.getProductsOrderedByPriceDesc(PageRequest.of(0, 9, Sort.Direction.DESC, "price"));
        log.info("getAllProductsOrderedByPriceDesc = {}", products);

        return new ResponseEntity<>(productResponseMapper.convertModelsToDtos(products), HttpStatus.OK);
    }

    @GetMapping("/products/categories/{category}")
    public ResponseEntity<List<ProductDTOResponse>> showProductsByCategory(@PathVariable String category) {

        categoryValidator.validate(category);

        List<Product> productsByCategory = productService.findProductByCategory(ProductCategory.valueOf(category.toUpperCase()));
        log.info("getProductsByCategory = {}", productsByCategory);

        return new ResponseEntity<>(productResponseMapper.convertModelsToDtos(productsByCategory), HttpStatus.OK);
    }

    @GetMapping("/products/{productCode}")
    public ResponseEntity<ProductDTOResponse> showOneProduct(@ApiParam(value = "The product's unique identification code", required = true) @PathVariable String productCode) {

        Product product = productService.findOneProduct(productCode);
        log.info("productDto = {}", product);

        return new ResponseEntity<>(productResponseMapper.convertModelToDto(product), HttpStatus.OK);
    }

    @GetMapping("/productCodesAndNames")
    public ResponseEntity<List<ProductCodeAndNameViewModel>> showProductCodes() {

        return new ResponseEntity<>(productService.getProductCodesAndNames(), HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTORequest> addProduct(@RequestBody ProductDTORequest productDtoRequest) {

        log.info("productDto = {}", productDtoRequest);
        productService.addProduct(productRequestMapper.convertDtoToModel(productDtoRequest));

        return new ResponseEntity<>(productDtoRequest, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productCode}")
    public void deleteProduct(@PathVariable String productCode) {

        productService.deleteProduct(productCode);
    }

    @PutMapping("/products/{productCode}")
    public void updateProduct(@RequestBody ProductDTORequest productDtoRequest, @PathVariable String productCode) {

        log.info("productDto = {}", productDtoRequest);

        //categoryValidator.validate(productDtoRequest.getCategory().getProductCategory());

        productService.updateProduct(productRequestMapper.convertDtoToModel(productDtoRequest), productCode);
    }
}
