package com.example.anphuc.api;

import com.example.anphuc.dto.OrderDTO;
import com.example.anphuc.dto.OrderItemDTO;
import com.example.anphuc.model.Account;
import com.example.anphuc.model.Order;
import com.example.anphuc.model.OrderDetail;
import com.example.anphuc.payload.response.APIResponse;
import com.example.anphuc.repository.AccountRepository;
import com.example.anphuc.repository.OrderRepository;
import com.example.anphuc.repository.OrderDetailRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderApi {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    HttpServletRequest request;

    @GetMapping("/order")
    public ResponseEntity<?> getOrder() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/order/account-{id}")
    public ResponseEntity<?> getOrderByAccount(@PathVariable Integer id) {
        return ResponseEntity.ok(orderRepository.findAllByAccount_Id(id));
    }

    @PutMapping("/public/order/confirm-{id}")
    public ResponseEntity<?> confirm(@PathVariable Integer id) {
        Order order = orderRepository.findById(id).get();
        order.setIsConfirm(true);
        orderRepository.save(order);

        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PutMapping("/public/order/cancel-{id}")
    public ResponseEntity<?> cancel(@PathVariable Integer id) {
        Order order = orderRepository.findById(id).get();
        order.setIsConfirm(false);
        orderRepository.save(order);
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok(new APIResponse(null));
    }

    @GetMapping("/orderDetail")
    public ResponseEntity<?> getOrderDetail() {
        return ResponseEntity.ok(orderDetailRepository.findAll());
    }

    @GetMapping("/orderDetail/{id}")
    public ResponseEntity<?> getOrderDetailById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderDetailRepository.findAllByOrders_Id(id));
    }

    @GetMapping("/public/total-order")
    public ResponseEntity<?> getTotalOrder() {
        int length = orderRepository.findAll().size();
        return ResponseEntity.ok(length);
    }

    @GetMapping("/public/total-order-product")
    public ResponseEntity<?> TotalOrderProduct() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        Integer total = 0;
        for (OrderDetail orderDetail : orderDetails) {
            total += orderDetail.getQuantity();
        }
        return ResponseEntity.ok(total);
    }

    @GetMapping("/public/total-order-price")
    public ResponseEntity<?> TotalOrderPrice() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        Integer total = 0;
        for (OrderDetail orderDetail : orderDetails) {
            total += orderDetail.getQuantity() * orderDetail.getProduct().getPrice();
        }
        return ResponseEntity.ok(total);
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOderDetail(@RequestBody OrderDTO dto) {
        Account acc = (Account) request.getAttribute("user");
        Order order = new Order();
        double totalPrice = 0;
        int totalQuantity = 0;
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderItemDTO item : dto.getOrderItemDTOS()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(item.getProduct());
            orderDetail.setQuantity(item.getQuantity());
            Double TotalPriceEachItem = Double.valueOf(item.getProduct().getPrice() * item.getQuantity());
            orderDetail.setTotal(TotalPriceEachItem);
            totalPrice += TotalPriceEachItem;
            totalQuantity += item.getQuantity();
            orderDetail.setOrders(order);
            orderDetailList.add(orderDetail);
        }
        totalPrice += dto.getShipFee();
        order.setOrderDetailList(orderDetailList);
        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setAddress(dto.getAddress());
        order.setAccount(acc);
        orderRepository.save(order);
        return ResponseEntity.ok(new APIResponse(null));
    }

}
