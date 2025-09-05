package com.example.catalog.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/payments/razorpay")
public class OrderRazorpayController  {

    
    @Value("${razorpay.key.id}") private String keyId;
    @Value("${razorpay.key.secret}") private String keySecret;

  @PostMapping("/create-order")
  public Map<String,Object> createOrder(@RequestBody Map<String,Object> body) throws RazorpayException {
    int amountInPaise = ((Number)body.get("amount")).intValue(); // amount in paise
    RazorpayClient client = new RazorpayClient(keyId, keySecret);

    JSONObject orderRequest = new JSONObject();
    orderRequest.put("amount", amountInPaise);
    orderRequest.put("currency", "INR");
    orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

    Order order = client.orders.create(orderRequest);

    Map<String,Object> resp = new HashMap<>();
    resp.put("orderId", order.get("id"));
    resp.put("amount", order.get("amount"));
    resp.put("key", keyId); // safe to send key id (not secret)
    return resp;
  }

}

