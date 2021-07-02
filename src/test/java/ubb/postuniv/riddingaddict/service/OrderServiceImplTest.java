package ubb.postuniv.riddingaddict.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ubb.postuniv.riddingaddict.exception.ItemNotFoundException;
import ubb.postuniv.riddingaddict.exception.ShopException;
import ubb.postuniv.riddingaddict.model.enums.AccessoryType;
import ubb.postuniv.riddingaddict.model.enums.BikeType;
import ubb.postuniv.riddingaddict.model.enums.CardType;
import ubb.postuniv.riddingaddict.model.orderFactory.FactoryProvider;
import ubb.postuniv.riddingaddict.model.pojo.*;
import ubb.postuniv.riddingaddict.repository.AppUserRepository;
import ubb.postuniv.riddingaddict.repository.OrderRepository;
import ubb.postuniv.riddingaddict.repository.ProductRepository;

import java.time.YearMonth;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceImplTest {

    private static final String PRODUCT_NAME = "Cube";
    private static final String PRODUCT_NAME_1 = "Lock";
    private static final String PRODUCT_CODE = "p0abc";
    private static final String PRODUCT_CODE_1 = "p1abc";
    private static final String PRODUCT_CODE_2 = "p2abc";
    private static final Double PRICE = 4000.00;
    private static final Integer QUANTITY = 12;

    private static final String CARDHOLDER_NAME = "customer1 c11";
    private static final String CARD_NUMBER = "1234432111223344";
    private static final Integer CVV_CODE = 123;

    private static final String USER_CODE = "ebs100";
    private static final String USERNAME = "johndoe";
    private static final String PASSWORD = "password";

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private AppUserRepository appUserRepositoryMock;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    private OrderServiceImpl underTest;

    private Product bike;
    private Product anotherBike;
    private Product bikeWith0Stock;
    private Product accessory;
    private Card card;

    private List<Product> productList;
    private List<Product> productList1;
    private List<Product> productList2;
    private Order order;
    private Order orderWithProductThatHas0Stock;
    private Order orderWithDiscount;

    private AppUser appUser;


    @BeforeAll
    void setup() {

        bike = ProductFactory.getProductRequest(PRODUCT_NAME, PRICE, QUANTITY, "", BikeType.MOUNTAINBIKE, null);
        bike.setProductCode(PRODUCT_CODE);

        bikeWith0Stock = ProductFactory.getProductRequest(PRODUCT_NAME, PRICE, 0, "", BikeType.MOUNTAINBIKE, null);
        bikeWith0Stock.setProductCode(PRODUCT_CODE);

        anotherBike = ProductFactory.getProductRequest(PRODUCT_NAME, PRICE, QUANTITY, "", BikeType.MOUNTAINBIKE, null);
        anotherBike.setProductCode(PRODUCT_CODE_2);

        accessory = ProductFactory.getProductRequest(PRODUCT_NAME_1, PRICE, QUANTITY, "", null, AccessoryType.LOCKS);
        accessory.setProductCode(PRODUCT_CODE_1);

        productList = Arrays.asList(bike, accessory);

        card = new Card(CARDHOLDER_NAME, CARD_NUMBER, CVV_CODE, YearMonth.of(2022, 11), CardType.VISA);

        Set<String> productCodes = new HashSet<>();
        productCodes.add(bike.getProductCode());
        productCodes.add(accessory.getProductCode());

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ADMIN"));

        appUser = new AppUser(USER_CODE, USERNAME, PASSWORD, roles);

        order = FactoryProvider.getFactory(productCodes.size()).getOrder();
        order.setAppUser(appUser);
        order.setCard(card);
        order.setProductCodes(productCodes);
        order.setProducts(productList);

        appUser.setOrders(Collections.singletonList(order));

        Set<String> productCodes1 = new HashSet<>();
        productCodes1.add(bikeWith0Stock.getProductCode());

        productList1 = Collections.singletonList(bikeWith0Stock);

        orderWithProductThatHas0Stock = FactoryProvider.getFactory(productCodes1.size()).getOrder();
        orderWithProductThatHas0Stock.setAppUser(appUser);
        orderWithProductThatHas0Stock.setCard(card);
        orderWithProductThatHas0Stock.setProductCodes(productCodes1);
        orderWithProductThatHas0Stock.setProducts(productList1);

        Set<String> productCodes2 = new HashSet<>();
        productCodes2.add(anotherBike.getProductCode());
        productCodes2.add(bike.getProductCode());
        productCodes2.add(accessory.getProductCode());

        productList2 = Arrays.asList(anotherBike, bike, accessory);

        orderWithDiscount = FactoryProvider.getFactory(productCodes2.size()).getOrder();
        orderWithDiscount.setAppUser(appUser);
        orderWithDiscount.setCard(card);
        orderWithDiscount.setProductCodes(productCodes2);
        orderWithDiscount.setProducts(productList2);

    }


    @Test
    void canFinalizePurchase() {

        //given
        given(productRepositoryMock.findByProductCode(PRODUCT_CODE)).willReturn(Optional.of(bike));
        given(productRepositoryMock.findByProductCode(PRODUCT_CODE_1)).willReturn(Optional.of(accessory));

        given(appUserRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(appUser));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(appUser.getUsername());

        //when
        //then
        assertThat(underTest.finalizePurchase(order).getTotalAmountPaid()).isEqualTo(8000.00);
    }

    @Test
    void testWillThrowExceptionIfProductHasZeroStock() {

        //given
        given(productRepositoryMock.findByProductCode(PRODUCT_CODE)).willReturn(Optional.of(bikeWith0Stock));
        given(appUserRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(appUser));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(appUser.getUsername());

        //when
        //then
        assertThatThrownBy(() -> underTest.finalizePurchase(orderWithProductThatHas0Stock))
                .isInstanceOf(ShopException.class)
                .hasMessageContaining("The product " + bikeWith0Stock.getName() + " has 0 stock");
    }

    @Test
    void testWillApplyDiscountIfOrderHasMoreThanTwoProducts() {

        //given
        given(productRepositoryMock.findByProductCode(PRODUCT_CODE)).willReturn(Optional.of(bike));
        given(productRepositoryMock.findByProductCode(PRODUCT_CODE_2)).willReturn(Optional.of(anotherBike));
        given(productRepositoryMock.findByProductCode(PRODUCT_CODE_1)).willReturn(Optional.of(accessory));

        given(appUserRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(appUser));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(appUser.getUsername());

        //when
        //then
        assertThat(underTest.finalizePurchase(orderWithDiscount).getTotalAmountPaid()).isEqualTo(10800.00);

    }

    @Test
    void canFinalizePurchaseWillThrowExceptionIfUserDoesNotExistInDatabase() {

        //given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        //when
        //then
        assertThatThrownBy(() -> underTest.finalizePurchase(order))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining("The user " + null + " was not found. Please register.");
    }

    @Test
    void canGetAllOrders() {

        //given
        given(appUserRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(appUser));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(appUser.getUsername());

        //when
        //then
        underTest.getAll();

        assertThat(underTest.getAll().size()).isEqualTo(1);
    }
}