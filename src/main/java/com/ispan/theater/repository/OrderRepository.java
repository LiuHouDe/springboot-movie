package com.ispan.theater.repository;

import com.ispan.theater.domain.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	//分頁  select * FROM Seat order by seat_id desc offset 0 rows fetch next 10 rows only 
	@Query(value="select o from Order o where o.user.id=:id")
	Optional<Order> findOrderByUserId(@Param("id")Integer id);
	
	@Modifying
	@Query(value="insert into \"Order\"(create_date,modify_date,order_amount,movie_id,user_id,payment_condition,order_status) values(:createDate,:modifyDate,:orderAmount,:movieId,:userId,:condition,:orderStatus)",nativeQuery=true)
	Integer createOrder(@Param("createDate")String createDate,@Param("modifyDate")String modifyDate,@Param("orderAmount")Double orderAmount,@Param("movieId")Integer movieId,@Param("userId")Integer userId,@Param("condition")Integer condition,@Param("orderStatus")Integer orderStatus);
	
	@Query(value="select top(1) o.* from \"Order\" as o where create_date like :createDate% and user_id=:id order by create_date desc",nativeQuery = true)
	Optional<Order> findOrderByUserIdAndCreateDate(@Param("createDate")String createDate,@Param("id")Integer id);
	
	//select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id join movie as m on m.movie_id=t.movie_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join \"Order\" as o on o.order_id=od.order_id where od.order_id= :orderId
	@Query(value="select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join movie as m on m.movie_id=o.movie_id  where od.order_id= :orderId",nativeQuery=true)
	List<Map<String,String>> orderCompleted(@Param("orderId")Integer orderId);	
	//select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join movie as m on m.movie_id=o.movie_id  where od.order_id= :orderId
	
	@Query(value="select ROW_NUMBER() over(order by Seat.seat_id asc) as 'no',od.order_id,c.location_category+c.name as 'location',m.name as movie_name,substring(CONVERT(varchar,s.Start_time),1,19) as Start_time,CONVERT(varchar, a.auditorium_number)+'廳'+CONVERT(varchar, Seat.seat_row)+'排'+CONVERT(varchar, seat_column)+'位' as seat,substring(CONVERT(varchar,o.create_date),1,19) as create_date from OrderDetail as od join Ticket as t on od.ticket_id=t.Ticket_id join Screening as s on t.Screening_id=s.Screening_id  join Seat on Seat.seat_id=t.seat_id join auditorium as a on a.auditorium_id=s.auditorium_id join cinema as c on a.cinema_id=c.cinema_id join \"Order\" as o on o.order_id=od.order_id join movie as m on m.movie_id=o.movie_id  where o.payment_no= :paymentNo",nativeQuery=true)
	List<Map<String,String>> orderCompletedByECPay(@Param("paymentNo")String paymentNo);
	
	//select od.order_id,count(*) as \"count\" from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.user_id= :userId group by od.order_id
	@Query(value="select od.order_id,count(*) as ticketCount from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.user_id= :userId group by od.order_id",nativeQuery=true)
	List<Map<String,String>> orderDetailCountByUserId(@Param("userId")Integer userId);
	
	@Query(value="select od.order_id,count(*) as \"count\" from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.order_id= :orderId group by od.order_id",nativeQuery=true)
	List<Map<String,Integer>> orderDetailCountByOrderId(@Param("orderId")Integer orderId);
	
	@Query(value="select od.order_id,count(*) as \"count\" from OrderDetail as od join \"Order\" as o on od.order_id=o.order_id where o.payment_condition=0 group by od.order_id",nativeQuery=true)
	List<Map<String,Integer>> orderDetailCountByPaymentCondition();
	
	@Modifying
	@Query(value="update \"Order\" set payment_condition=1 where order_id= :orderId",nativeQuery=true)
	void setOrderConditionByUserId(@Param("orderId")Integer orderId);
	
	@Modifying
	@Transactional
	@Query(value="update \"Order\" set payment_condition=1,order_status=1 where payment_no= :paymentNo",nativeQuery=true)
	void setOrderConditionByPaymentNo(@Param("paymentNo")String paymentNo);
	
	@Modifying
	@Transactional
	@Query(value="update \"Order\" set payment_no= :paymentNo where order_id= :orderId",nativeQuery=true)
	void setPaymentNoByOrderId(@Param("paymentNo")String paymentNo,@Param("orderId")Integer orderId);
	
	@Modifying
	@Transactional
	@Query(value="update \"Order\" set payment_no=:paymentNo,payment_condition=1,order_status=1 where order_id= :orderId",nativeQuery=true)
	void setPaymentNoAndConditionByOrderId(@Param("paymentNo")String paymentNo,@Param("orderId")Integer orderId);
	
	
	@Query(value="select ROW_NUMBER() over(order by o.create_date desc) as 'no',o.order_id,m.name,SUBSTRING(convert(varchar(19),o.create_date),1,19) as create_date,o.order_amount,o.supplier,o.order_status FROM \"Order\" as o join movie as m on o.movie_id=m.movie_id where o.user_id=:userId order by o.create_date desc offset :page rows fetch next 10 rows only ",nativeQuery=true)
	List<Map<String,String>> getOrderByUser(@Param("userId")Integer userId,@Param("page")Integer page);
	
	@Query(value="select ROW_NUMBER() over(order by o.create_date desc) as 'no',o.order_id,u.email,m.name,SUBSTRING(convert(varchar(19),o.create_date),1,19) as create_date,o.order_amount,o.supplier,o.order_status FROM \"Order\" as o join movie as m on o.movie_id=m.movie_id join \"user\" as u on u.user_id=o.user_id order by o.create_date desc offset :page rows fetch next 10 rows only ",nativeQuery=true)
	List<Map<String,String>> getOrder(@Param("page")Integer page);
	
	@Query(value="select count(order_id) as order_total from \"Order\" where user_id=:userId",nativeQuery=true)
	Map<String,Integer> orderTotalByUserId(@Param("userId")Integer userId);
	
	@Query(value="select count(order_id) as order_total from \"Order\"",nativeQuery=true)
	Map<String,Integer> orderTotal();
	
	@Modifying
	@Query(value="update t set t.is_available= '未售出' from  Ticket as t join OrderDetail as od on t.Ticket_id=od.ticket_id where od.order_id in (select od.order_id from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id where o.payment_condition=0 and DATEDIFF(s,substring(convert(varchar,o.create_date),1,19),convert(nvarchar,getDate(),120)) > 600 group by od.order_id)",nativeQuery=true)
	void deleteOrderStep1();
	@Modifying
	@Query(value=" delete from OrderDetail where order_id in (select od.order_id from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id where o.payment_condition=0 and DATEDIFF(s,substring(convert(varchar,o.create_date),1,19),convert(nvarchar,getDate(),120)) > 600 group by od.order_id) ",nativeQuery=true)
	void deleteOrderStep2();
	@Modifying
	@Query(value="delete from \"Order\" where payment_condition=0 and DATEDIFF(s,substring(convert(varchar,create_date),1,19),convert(varchar,getDate(),120))>600",nativeQuery=true)
	void deleteOrderStep3();
	
	@Modifying
	@Query(value="update t set t.is_available= '未售出' from  Ticket as t join OrderDetail as od on t.Ticket_id=od.ticket_id where od.order_id in (select od.order_id from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id where o.payment_condition=0 and o.user_id=:userId)",nativeQuery=true)
	void deleteOrderStep1Version2(@Param("userId")Integer userId);
	@Modifying
	@Query(value=" delete from OrderDetail where order_id in (select od.order_id from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id where o.payment_condition=0 and o.user_id=:userId) ",nativeQuery=true)
	void deleteOrderStep2Version2(@Param("userId")Integer userId);
	@Modifying
	@Query(value="delete from \"Order\" where payment_condition=0 and user_id=:userId",nativeQuery=true)
	void deleteOrderStep3Version2(@Param("userId")Integer userId);
	
	@Modifying
	@Query(value="update t set t.is_available= '未售出' from  Ticket as t  where t.Ticket_id in (select od.ticket_id from OrderDetail as od where od.order_id=:orderId)",nativeQuery=true)
	void orderRefundStep1(@Param("orderId")Integer orderId);
	@Modifying
	@Query(value="update \"Order\" set order_status=0 where order_id=:orderId",nativeQuery=true)
	void orderRefundStep2(@Param("orderId")Integer orderId);
	
	@Modifying
	@Query(value="update \"user\" set consumption=consumption+(select o.order_amount from \"Order\" as o where o.order_id=:orderId) where user_id=(select user_id from \"Order\" as o where o.order_id=:orderId)",nativeQuery=true)
	void setUserConsumption(@Param("orderId")Integer orderId);
	
	//update "user" set consumption=+(select o.order_amount from "Order" as o where o.payment_no='aaa4ff026f254301a5c6') where user_id=(select user_id from "Order" as o where o.payment_no='aaa4ff026f254301a5c6') 
	@Modifying
	@Query(value="update \"user\" set consumption=consumption+(select o.order_amount from \"Order\" as o where o.payment_no=:paymentNo) where user_id=(select user_id from \"Order\" as o where o.payment_no=:paymentNo)",nativeQuery=true)
	void setUserConsumptionECPay(@Param("paymentNo")String paymentNo);
	
//	@Modifying
//	@Query(value="delete from OrderDetail where order_id =:orderId",nativeQuery=true)
//	void orderRefundStep2(@Param("orderId")Integer orderId);
//	@Modifying
//	@Query(value="delete from \"Order\" where order_id=:orderId",nativeQuery=true)
//	void orderRefundStep3(@Param("orderId")Integer orderId);
	
	@Query(value="select datediff(second,SUBSTRING(CONVERT(nvarchar,s.End_time),1,19),dateadd(second,28800,convert(nvarchar,getdate(),120))) from OrderDetail as od join Ticket as t on t.Ticket_id=od.ticket_id join Screening as s on s.Screening_id=t.Screening_id where od.order_id= :orderId group by s.End_time",nativeQuery=true)
	Optional<Integer> findOrderConditionCurrentDate(@Param("orderId")Integer orderId);
	
}