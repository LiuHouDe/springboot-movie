package com.ispan.theater.controller;

import com.ispan.theater.domain.Order;
import com.ispan.theater.dto.PaymentRequest;
import com.ispan.theater.repository.OrderRepository;
import com.ispan.theater.service.OrderService;
import com.ispan.theater.service.PaypalService;
import com.ispan.theater.service.TicketService;
import com.ispan.theater.util.JsonWebTokenUtility;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/paypal")
public class PaypalController {
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    String cancelUrl = "https://httpbin.org/get?paymentStatus=cancelled";
    String successUrl =  "/order/paymentsuccess";
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TicketService ticketService;

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        //String data = jsonWebTokenUtility.validateEncryptedToken(token);
        try {
            Payment payment = paypalService.createPayment(
                    paymentRequest.getTotal(), paymentRequest.getCurrency(),
                    "paypal", "sale", "Payment Description", cancelUrl,
                    successUrl);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
            return ResponseEntity.ok("No approval URL FOUND");
        } catch (PayPalRESTException e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }


    }

    @GetMapping("/execute")
    public  ResponseEntity<?> executePayment(@RequestParam String PayerID, @RequestParam String paymentId,@RequestParam Integer orderId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, PayerID);
            String saleid = paypalService.getSaleIdFromPayment(payment);
            System.out.println(paymentId);
            Order order = orderService.findOrderByOrderId(orderId);
            if ("approved".equals(payment.getState())) {
                order.setPaymentNo(saleid);
                order.setPaymentCondition(true);
                order.setOrderStatus(true);
                orderRepository.save(order);
                return ResponseEntity.ok(new HashMap<String, String>() {{
                    put("status", "success");
                    put("url", "/order/findOrder");
                }});
            } else {
                order.setPaymentNo(saleid);
                order.setPaymentCondition(false);
                orderRepository.save(order);
                //paypalService.insertPaypalOrder(paymentId,PayerID,1,"付款失敗",saleid);
                return ResponseEntity.ok(new HashMap<String, String>() {{
                    put("status", "failure");
                    put("url", "/order/findOrder");
                }});
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("status", "error");
                put("url", "/order/findOrder");
            }});
        }
    }
    @PostMapping("/refund")
    public ResponseEntity<?> refundPaypal(@RequestParam("orderId") Integer orderId) {
        String Orderdetail = orderService.getTicketfromDetail(orderId);
        System.out.println(Orderdetail);
        try {
            Order order = orderService.findOrderByOrderId(orderId);
            String saleId = order.getPaymentNo();
            String refundStatus = paypalService.refundPayment(saleId,order);
//            String refundStatus = "success";
            if ("success".equals(refundStatus)) {
                JSONObject obj = new JSONObject(Orderdetail);
                JSONArray details = obj.getJSONArray("details");
                List<Integer> orderIds = new ArrayList<>();
                for (int i = 0; i < details.length(); i++) {
                    JSONObject detail = details.getJSONObject(i);
                    int id = detail.getInt("ticket_id");
                    orderIds.add(id);
                }
                ticketService.setTicketStatuaToNotSale(orderIds);
                return ResponseEntity.ok(new HashMap<String, Object>() {{
                    put("status", "success");
                    put("message", "Refund successful");
                }});
            } else {
                return ResponseEntity.ok(new HashMap<String, Object>() {{
                    put("status", "failure");
                    put("message", "Refund failed");
                }});
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, Object>() {{
                put("status", "error");
                put("message", "Error processing refund: " + e.getMessage());
            }});
        }
    }
    @GetMapping("/")
    public String home() {
        return "Welcome to the Spring Boot App!";
    }


}
