package com.ispan.theater.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ispan.theater.domain.Movie;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Order;
import com.ispan.theater.domain.OrderDetail;
import com.ispan.theater.domain.Ticket;
import com.ispan.theater.dto.InsertOrderDTO;

import com.ispan.theater.exception.OrderException;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.OrderDetailRepository;
import com.ispan.theater.repository.OrderRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.repository.TicketRepository;
import com.ispan.theater.util.DatetimeConverter;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	ScreeningRepository screeningRepository;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	@Autowired
	LinePayService linePayService;
	@Autowired
	ECPayService ecPayService;
	@Autowired
	PaypalService paypalService;

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Order findOrderByOrderId(Integer id) {
		Optional<Order> order = orderRepository.findById(id);
		return order.orElse(null);
	}

	public Order findOrderByUserId(Integer id) {
		Optional<Order> order = orderRepository.findOrderByUserId(id);
		return order.orElse(null);
	}

	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

	@Transactional
	public void updeteOrderAmount(Order order) {
		order.setOrderAmount(order.getOrderAmount() + 1);
	}

	@Transactional
	public void updeteOrderAmountTest() {
		Order order = orderRepository.findOrderByUserId(3).orElse(null);
		order.setOrderAmount(order.getOrderAmount() + 1);
	}

	@Transactional
	public void setTicket(Ticket ticket) {
		ticket.setIsAvailable("已售出");
	}

	public List<Map<String, Object>> findMovieByScreening(Integer cinemaId) {
		return movieRepository.findMovieByScreening(cinemaId);
	}

	public List<Map<String, Object>> findScreeningByDate(Integer cinemaId, Integer movieId) {
		return screeningRepository.findScreeningByDate(cinemaId, movieId);
	}

	public List<Map<String, Object>> findScreeningByTime(Integer cinemaId, String Date, Integer movieId) {
		return screeningRepository.findScreeningByTime(cinemaId, Date, movieId);
	}

	public List<Map<String, Object>> ticketList(Integer id) {
		orderRepository.orderDetailCountByPaymentCondition().forEach(map -> System.out.println(map.entrySet()));
		return ticketRepository.getTickets(id);
	}

//	public List<Map<String,Object>> ticketList1(Integer id){
//		return ticketRepository.getTickets(id);
//	}

	@Transactional
	public String createOrder(InsertOrderDTO insertOrderDto) {
		String Date = DatetimeConverter.createSqlDatetime(new Date(System.currentTimeMillis()+28800000));
		Order order = null;
		String result = "";
		Double price = 0.0;
		Movie movie = movieRepository.findById(insertOrderDto.getMovieId()).orElse(null);
		if (movie != null) {
			price = movie.getPrice();
		}
		Integer count = orderRepository.createOrder(Date, Date, (price* (insertOrderDto.getTicketId().size())),
				insertOrderDto.getMovieId(), insertOrderDto.getUserId(), 0 ,0);
		if (count > 0) {
			order = orderRepository.findOrderByUserIdAndCreateDate(Date, insertOrderDto.getUserId()).get();

		}
		List<Ticket> tickets = ticketRepository.findTicketsById(insertOrderDto.getTicketId());
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		for (int i = 0; i < tickets.size(); i++) {
			orderDetails.add(new OrderDetail(order, tickets.get(i)));
		}
		orderDetailRepository.saveAll(orderDetails);
		for (int i = 0; i < tickets.size(); i++) {
			if (!"未售出".equals(tickets.get(i).getIsAvailable())) {
				throw new OrderException(HttpStatus.SC_BAD_REQUEST,"已有座位被售出，請重新選擇！");
			}
		}
		ticketRepository.setTicketAvailable("已售出", insertOrderDto.getTicketId());
		if ("linePay".equals(insertOrderDto.getPaymentOptions())) {
			order.setSupplier("linepay");
			result = linePayService.request(order, insertOrderDto.getTicketId().size()).get("info").get("paymentUrl").get("web");
		}
		if ("ecPay".equals(insertOrderDto.getPaymentOptions())) {
			String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
			order.setPaymentNo(uuId);
			order.setSupplier("ecpay");
			result = ecPayService.ecpayCheckout(order, insertOrderDto.getTicketId().size());

		}
		if("paypal".equals(insertOrderDto.getPaymentOptions())){
//			String paypalsuccessUrl =  "http://localhost:5173/order/paymentsuccess?orderId=" + order.getId();
//			String paypalcancelUrl = "http://localhost:5173/order/findOrder";
			String paypalsuccessUrl =  "https://starburst-cinema.xyz/order/paymentsuccess?orderId=" + order.getId();
			String paypalcancelUrl = "https://starburst-cinema.xyz/order/findOrder";
			https://vue-6kn3sj32ha-de.a.run.app/
			try {
				order.setSupplier("paypal");
				Payment payment = paypalService.createPayment(order.getOrderAmount(),"TWD","paypal", "sale", "Payment Description", paypalcancelUrl,
						paypalsuccessUrl);
				for (Links link : payment.getLinks()) {
					if (link.getRel().equals("approval_url")) {
						System.out.println(link.getHref());
						return link.getHref();
					}
				}
				result =  "No approval URL FOUND";
			} catch (PayPalRESTException e) {
				result ="ERROR: " + e.getMessage();
			}

		}
		return result;
	}

	@Transactional
	public String orderCompleted(String transactionId, Integer orderId) {
		System.out.println(linePayService.confirm(transactionId, orderId).get("returnCode"));// returnCode為0000
		orderRepository.setPaymentNoAndConditionByOrderId(transactionId, orderId);
		orderRepository.setUserConsumption(orderId);
		return new JSONObject().put("Order", orderRepository.orderCompleted(orderId)).toString();
	}

	public String getOrder(Integer userId, Integer page) {
		Integer total = orderRepository.orderTotalByUserId(userId).get("order_total");
		Integer pages = 1;
		if (total % 10 == 0 && total / 10 != 0) {
			pages = total / 10;
		} else if (total % 10 != 0 && total / 10 != 0) {
			pages = (total / 10) + 1;
		}
		return new JSONObject().put("orders", orderRepository.getOrderByUser(userId, (page - 1) * 10))
				.put("pages", pages).toString();
	}

	public String getOrderBackStage(Integer page) {
		Integer total = orderRepository.orderTotal().get("order_total");
		Integer pages = 1;
		if (total % 10 == 0 && total / 10 != 0) {
			pages = total / 10;
		} else if (total % 10 != 0 && total / 10 != 0) {
			pages = (total / 10) + 1;
		}
		return new JSONObject().put("orders", orderRepository.getOrder((page - 1) * 10))
				.put("pages", pages).toString();
	}
	
	public String getOrderDetail(Integer orderId) {
		return new JSONObject().put("details", orderRepository.orderCompleted(orderId)).toString();
	}

	@Transactional
	public void deleteOrder() {
		orderRepository.deleteOrderStep1();
		orderRepository.deleteOrderStep2();
		orderRepository.deleteOrderStep3();
	}

	@Transactional
	public void deleteOrderV2(Integer userId) {
		orderRepository.deleteOrderStep1Version2(userId);
		orderRepository.deleteOrderStep2Version2(userId);
		orderRepository.deleteOrderStep3Version2(userId);
	}
	
	@Transactional
	public String refund(Integer orderId)  {
		Integer expirations=orderRepository.findOrderConditionCurrentDate(orderId).orElseThrow(()->new OrderException(1001,"場次已結束！無法退款！"));
		if(expirations>=0) {
			throw new OrderException(901,"場次已結束！無法退款！");
		}
		Order order = orderRepository.findById(orderId).get();
		String returnCode = "";
		if ("linepay".equals(order.getSupplier())) {
			returnCode = linePayService.refund(order.getPaymentNo()).get("returnCode");
			if (!("0000".equals(returnCode) || "1165".equals(returnCode))) {
				return new JSONObject().put("success", false).put("message", "退款失敗！請聯繫金流方").toString();
			}
		}else if("ecpay".equals(order.getSupplier())) {
			String result=ecPayService.postQueryTradeInfo(order.getPaymentNo());
			String tradeNo=result.substring(result.indexOf("&TradeNo")+9, result.indexOf("&",result.indexOf("&TradeNo")+1));
			String amount=result.substring(result.indexOf("&amount")+8, result.indexOf("&",result.indexOf("&amount")+1));
			String resultMessage=ecPayService.postDoAction(order.getPaymentNo(), amount, tradeNo);
			if(!"1".equals(resultMessage.substring(resultMessage.indexOf("&RtnCode")+9, resultMessage.indexOf("&",resultMessage.indexOf("&RtnCode")+1)))) {
				return new JSONObject().put("success", false).put("message", "退款失敗！請聯繫金流方").toString();
			}
		}
		else if("paypal".equals(order.getSupplier())){
			String status = paypalService.refundPayment(order.getPaymentNo(),order);
			if(status.equals("fail")){
				return new JSONObject().put("success", false).put("message", "退款失敗！請聯繫金流方").toString();
			}
		}
	    orderRepository.orderRefundStep1(orderId);
	    orderRepository.orderRefundStep2(orderId);
	    return new JSONObject().put("success", true).put("message", "成功退款！").toString();
	}
	@Transactional
	public String completePaypalOrder(String paymentId, String payerId, Integer orderId) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			String saleId = paypalService.getSaleIdFromPayment(payment);
			if (payment.getState().equals("approved")) {
				orderRepository.setPaymentNoAndConditionByOrderId(saleId, orderId);
				return new JSONObject().put("success", true).put("order", orderRepository.orderCompleted(orderId)).toString();
			}
		} catch (PayPalRESTException e) {
			return new JSONObject().put("success", false).put("error", e.getMessage()).toString();
		}
		return new JSONObject().put("success", false).toString();
	}
	public String getTicketfromDetail(Integer orderId){
		return new JSONObject().put("details", orderDetailRepository.getTicketDetail(orderId)).toString();
	}

}
