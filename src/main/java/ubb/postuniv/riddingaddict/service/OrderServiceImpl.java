package ubb.postuniv.riddingaddict.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ubb.postuniv.riddingaddict.exception.ItemNotFoundException;
import ubb.postuniv.riddingaddict.exception.ShopException;
import ubb.postuniv.riddingaddict.model.pojo.AppUser;
import ubb.postuniv.riddingaddict.model.pojo.BigOrder;
import ubb.postuniv.riddingaddict.model.pojo.Order;
import ubb.postuniv.riddingaddict.model.pojo.Product;
import ubb.postuniv.riddingaddict.repository.AppUserRepository;
import ubb.postuniv.riddingaddict.repository.OrderRepository;
import ubb.postuniv.riddingaddict.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AppUserRepository appUserRepository;


    @Override
    public Order finalizePurchase(Order order) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() ->
                new ItemNotFoundException("The user " + username + " was not found. Please register."));

        order.setAppUser(appUser);

        List<Product> productList = order.getProductCodes().stream()
                .map(code -> productRepository.findByProductCode(code).orElseThrow(() ->
                        new ItemNotFoundException("The product with code " + code + " does not exist")))
                .collect(Collectors.toList());

        order.setProducts(productList);

        appUser.getProducts().addAll(order.getProducts());
        appUserRepository.save(appUser);

        decreaseQuantity(productList);

        order.setTotalAmountPaid(computeTotalPrice(order));

        orderRepository.save(order);

        return order;
    }

    private void decreaseQuantity(List<Product> productList) {

        productList.forEach(product -> {

            if (product.getQuantity() > 0) {

                product.setQuantity(product.getQuantity() - 1);
                productRepository.updateQuantity(product.getQuantity(), product.getId());

            } else {

                throw new ShopException("The product " + product.getName() + " has 0 stock");
            }
        });
    }

    private double computeTotalPrice(Order order) {

        double totalAmount = order.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        if (order instanceof BigOrder) {

            totalAmount -= totalAmount * ((BigOrder) order).getDiscount();
        }
        return totalAmount;
    }

    @Override
    public List<Order> getAll() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() ->
                new ItemNotFoundException("The user " + username + " was not found. Please register."));

        return appUser.getOrders();
    }
}
