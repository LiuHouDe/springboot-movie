package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "paypalorder")
public class PaypalOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "order_id", nullable = false)
//    @JsonIgnore
//    private Order order;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

    @Column(name = "payerId", nullable = false)
    private String payerId;

    @Column(name = "paymentId", nullable = false)
    private String paymentId;

    @Column(name = "saleId", nullable = false)
    private String saleId;

    @Column(name = "status", nullable = false)
    private String status;

    @PrePersist
    public void onCreate() {
        if (createDate == null) {
            createDate = new Date();
        }
        if (modifyDate == null) {
            modifyDate = new Date();
        }
    }

    @Override
    public String toString() {
        return "PaypalOrder{" +
                "id=" + id +
                ", payerId='" + payerId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                '}';
    }
}
