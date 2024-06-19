package com.ispan.theater.repository;

import com.ispan.theater.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query(value="select od.order_id,od.ticket_id from OrderDetail as od join \"Order\" as o on o.order_id=od.order_id join Ticket as t on od.ticket_id=t.Ticket_id where od.order_id= :orderId",nativeQuery=true)
    List<Map<String,String>> getTicketDetail(@Param("orderId")Integer orderId);
}