package ubb.postuniv.riddingaddict.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ubb.postuniv.riddingaddict.exception.ItemNotFoundException;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.ProductCategory;
import ubb.postuniv.riddingaddict.model.pojo.Product;
import ubb.postuniv.riddingaddict.model.pojo.ProductFactory;
import ubb.postuniv.riddingaddict.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceImplTest {

    private static final String PRODUCT_NAME = "Cube";
    private static final String PRODUCT_CODE = "p1abc";
    private static final Double PRICE = 4000.00;
    private static final Integer QUANTITY = 12;

    @Mock
    private ProductRepository productRepositoryMock;

    @InjectMocks
    private ProductServiceImpl underTest;

    private Product bike;
    private List<Product> productList;
    private PageRequest pageable;


    @BeforeAll
    void setup() {

        bike = ProductFactory.getProductRequest(PRODUCT_NAME, PRICE, QUANTITY, "", BikeType.MOUNTAINBIKE, null);
        bike.setProductCode(PRODUCT_CODE);

        productList = Collections.singletonList(bike);

        pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "price");
    }

    @Test
    void canAddProduct() {

        //when
        underTest.addProduct(bike);

        //then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepositoryMock)
                .save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct).isEqualTo(bike);
    }

    @Test
    void canFindOneProduct() {

        //given
        given(productRepositoryMock.findByProductCode(bike.getProductCode())).willReturn(Optional.of(bike));

        //when
        //then
        assertThat(underTest.findOneProduct(bike.getProductCode())).isEqualTo(bike);
    }

    @Test
    void canGetAllProducts() {

        // when
        underTest.getAll();

        //then
        verify(productRepositoryMock).findAll();
    }

    @Test
    void testWillThrowExceptionIfProductIdDoesNotExistInDatabase() {

        //given
        given(productRepositoryMock.findByProductCode(bike.getProductCode())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.findOneProduct(bike.getProductCode()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("The product with code " + PRODUCT_CODE + " does not exist");

    }

    @Test
    void canGetProductsOrderedByPriceDesc() {

        //when
        underTest.getProductsOrderedByPriceDesc(pageable);

        //then
        verify(productRepositoryMock).findAllByOrderByPriceDesc(pageable);
    }

    @Test
    void canFindProductByCategory() {

        //given
        given(productRepositoryMock.findByCategory(ProductCategory.BIKE)).willReturn(productList);

        //when
        underTest.findProductByCategory(ProductCategory.BIKE);

        //then
        verify(productRepositoryMock).findByCategory(ProductCategory.BIKE);

        assertThat(underTest.findProductByCategory(ProductCategory.BIKE).size()).isEqualTo(1);
    }
}