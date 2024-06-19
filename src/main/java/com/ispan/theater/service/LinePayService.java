package com.ispan.theater.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ispan.theater.domain.Order;
import com.ispan.theater.linepay.CheckoutPaymentRequestForm;
import com.ispan.theater.linepay.ConfirmData;
import com.ispan.theater.linepay.ProductForm;
import com.ispan.theater.linepay.ProductPackageForm;
import com.ispan.theater.linepay.RedirectUrls;
import com.ispan.theater.linepay.RefundData;
import com.ispan.theater.repository.OrderRepository;

@Service
public class LinePayService {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	RestTemplate restTemplate;
	
	public static String encrypt(final String keys, final String data) {
		return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
	}

	public static String toBase64String(byte[] bytes) {
		byte[] byteArray = Base64.encodeBase64(bytes);
		return new String(byteArray);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Map<String,Map<String,String>>> request(Order order,Integer quantity) {
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();

		form.setAmount(new BigDecimal(order.getOrderAmount()));
		form.setCurrency("TWD");
		form.setOrderId(order.getId().toString());

		ProductPackageForm productPackageForm = new ProductPackageForm();
		productPackageForm.setId("package_id");
		productPackageForm.setName("shop_movie");
		productPackageForm.setAmount(new BigDecimal(order.getOrderAmount()));

		ProductForm productForm = new ProductForm();
		productForm.setId("product_id");
		productForm.setName("電影票"+quantity+"張");
		productForm.setImageUrl("");
		productForm.setQuantity(new BigDecimal(quantity));
		productForm.setPrice(new BigDecimal(order.getMovie().getPrice()));
		productPackageForm.setProducts(Arrays.asList(productForm));

		form.setPackages(Arrays.asList(productPackageForm));
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setConfirmUrl("https://starburst-cinema.xyz/order/complete");
		redirectUrls.setCancelUrl("");
		form.setRedirectUrls(redirectUrls);
	
		ObjectMapper mapper=new ObjectMapper();
		String ChannelSecret = "cc93c047b1eb1ec2d9cad8561b942310";
		String requestUri = "/v3/payments/request";
		String nonce = UUID.randomUUID().toString();

		Map<String,Map<String,Map<String,String>>> reponse=null;
		try {
			String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			HttpHeaders httpHeaders=new HttpHeaders();
			httpHeaders.add("X-LINE-ChannelId", "2005109267");
			httpHeaders.add("X-LINE-Authorization-Nonce", nonce);
			httpHeaders.add("X-LINE-Authorization", signature);
			httpHeaders.add("Content-Type", "application/json");
			HttpEntity<String> httpEntity=new HttpEntity<>(mapper.writeValueAsString(form),httpHeaders);
			reponse=restTemplate.postForObject("https://sandbox-api-pay.line.me/v3/payments/request", httpEntity, Map.class);
			System.out.println(reponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return reponse;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Map<String,Map<String,Map<String,String>>> confirm(String transactionId,Integer orderId) {
		//Confiirm API
		ConfirmData confirrmData=new ConfirmData();
		confirrmData.setAmount(new BigDecimal(orderRepository.findById(orderId).get().getOrderAmount()));
		confirrmData.setCurrency("TWD");
		String confirmNonce=UUID.randomUUID().toString();
		String comfirmUrl="/v3/payments/"+transactionId+"/confirm";
		String ChannelSecret = "cc93c047b1eb1ec2d9cad8561b942310";
		ObjectMapper mapper=new ObjectMapper();
		Map<String,Map<String,Map<String,String>>> reponse=null;
		try {
			String signatureConfirm = encrypt(ChannelSecret, ChannelSecret + comfirmUrl + mapper.writeValueAsString(confirrmData) + confirmNonce);
			HttpHeaders httpHeaders=new HttpHeaders();
			httpHeaders.add("X-LINE-ChannelId", "2005109267");
			httpHeaders.add("X-LINE-Authorization-Nonce", confirmNonce);
			httpHeaders.add("X-LINE-Authorization",signatureConfirm);
			httpHeaders.add("Content-Type", "application/json");
			HttpEntity<String> httpEntity=new HttpEntity<>(mapper.writeValueAsString(confirrmData),httpHeaders);
			reponse=restTemplate.postForObject("https://sandbox-api-pay.line.me/v3/payments/"+transactionId+"/confirm", httpEntity, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return reponse;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> refund(String transactionId){
		
		RestTemplate restTemplate=new RestTemplate();

		RefundData refundData=new RefundData("");
		String refundNonce=UUID.randomUUID().toString();
		String refundUrl="/v3/payments/"+transactionId+"/refund";
		String ChannelSecret = "cc93c047b1eb1ec2d9cad8561b942310";
		ObjectMapper mapper=new ObjectMapper();
		Map<String,String> reponse=null;
		
		try {
			String signatureRefund = encrypt(ChannelSecret, ChannelSecret + refundUrl + mapper.writeValueAsString(refundData) + refundNonce);
			HttpHeaders httpHeaders=new HttpHeaders();
			httpHeaders.add("X-LINE-ChannelId", "2005109267");
			httpHeaders.add("X-LINE-Authorization-Nonce", refundNonce);
			httpHeaders.add("X-LINE-Authorization",signatureRefund);
			httpHeaders.add("Content-Type", "application/json");
			HttpEntity<String> httpEntity=new HttpEntity<>(mapper.writeValueAsString(refundData),httpHeaders);
			reponse=restTemplate.postForObject("https://sandbox-api-pay.line.me/v3/payments/"+transactionId+"/refund", httpEntity, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return reponse;
	}
	
}
