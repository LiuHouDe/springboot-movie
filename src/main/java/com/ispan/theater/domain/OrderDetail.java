package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "OrderDetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetail_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    @Column(name = "used", columnDefinition = "bit")
    private Boolean used;

    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", order=" + order + ", ticket=" + ticket + "]";
    }

    public OrderDetail(Order order, Ticket ticket) {
        super();
        this.order = order;
        this.ticket = ticket;
    }

}