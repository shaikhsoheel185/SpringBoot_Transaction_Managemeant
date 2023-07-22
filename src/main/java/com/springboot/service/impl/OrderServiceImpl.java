package com.springboot.service.impl;

import com.springboot.dto.OrderRequest;
import com.springboot.dto.OrderResponse;
import com.springboot.entity.Order;
import com.springboot.entity.Payment;
import com.springboot.repositry.OrderRepositry;
import com.springboot.repositry.PaymentRepositry;
import com.springboot.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {


    private OrderRepositry orderRepositry;
    private PaymentRepositry paymentRepositry;

    public OrderServiceImpl(OrderRepositry orderRepositry, PaymentRepositry paymentRepositry) {
        this.orderRepositry = orderRepositry;
        this.paymentRepositry = paymentRepositry;
    }

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {

       Order order =orderRequest.getOrder();
       order.setStatus("INPROGRESS");

       orderRepositry.save(order);

       Payment payment =orderRequest.getPayment();

       if (!payment.getType().equals("debit")){
           throw  new RuntimeException("Payment Card Type Don Not Support");
       }

       payment.setOrderId(order.getId());
       paymentRepositry.save(payment);

       OrderResponse orderResponse = new OrderResponse();
       orderResponse.setOrderTrackingNumber(order.getOrderTrackingNumber());
       orderResponse.setStatus(order.getStatus());
       orderResponse.setMessage("SUCESS");

       return orderResponse;

    }
}
