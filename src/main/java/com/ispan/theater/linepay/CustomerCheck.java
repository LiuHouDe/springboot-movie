package com.ispan.theater.linepay;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerCheck {

	public static String encrypt(final String keys, final String data) {
		return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
	}

	public static String toBase64String(byte[] bytes) {
		byte[] byteArray = Base64.encodeBase64(bytes);
		return new String(byteArray);
	}

	public static void main(String[] args) {
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();

		form.setAmount(new BigDecimal("100"));
		form.setCurrency("TWD");
		form.setOrderId("merchant_order_id_014");

		ProductPackageForm productPackageForm = new ProductPackageForm();
		productPackageForm.setId("package_id");
		productPackageForm.setName("shop_name");
		productPackageForm.setAmount(new BigDecimal("100"));

		ProductForm productForm = new ProductForm();
		productForm.setId("product_id");
		productForm.setName("product_name");
		productForm.setImageUrl("");
		productForm.setQuantity(new BigDecimal("10"));
		productForm.setPrice(new BigDecimal("10"));
		productPackageForm.setProducts(Arrays.asList(productForm));

		form.setPackages(Arrays.asList(productPackageForm));
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setConfirmUrl("http://localhost:5173/");
		redirectUrls.setCancelUrl("");
		form.setRedirectUrls(redirectUrls);
		
		//Confiirm API
		ConfirmData confirrmData=new ConfirmData();
		confirrmData.setAmount(new BigDecimal("100"));
		confirrmData.setCurrency("TWD");
		String confirmNonce=UUID.randomUUID().toString();
		String comfirmUrl="/v3/payments/2024051702120872410/confirm";
		//Request API
		ObjectMapper mapper=new ObjectMapper();
		String ChannelSecret = "cc93c047b1eb1ec2d9cad8561b942310";
		String requestUri = "/v3/payments/request";
		String nonce = UUID.randomUUID().toString();
		try {
			//Request API			
			System.out.println("body->"+mapper.writeValueAsString(form));
			System.out.println("nonce->"+nonce);
			String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			System.out.println("signature->"+signature);
			//Confiirm API
			System.out.println("bodyConfirm->"+mapper.writeValueAsString(confirrmData));
			System.out.println("nonceConfirm->"+confirmNonce);
			String signatureConfirm = encrypt(ChannelSecret, ChannelSecret + comfirmUrl + mapper.writeValueAsString(confirrmData) + confirmNonce);
			System.out.println("signatureConfirm->"+signatureConfirm);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
