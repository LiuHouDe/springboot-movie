package com.ispan.theater.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"Order\"")
public class Order implements Persistable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

    @Column(name = "order_amount", nullable = false)
    private Double orderAmount;
    
    @Column(name = "payment_condition", nullable = false,columnDefinition ="bit default 0")
    private boolean paymentCondition;
    
    @Column(name = "order_status", nullable = false,columnDefinition ="bit default 0")
    private boolean orderStatus;

    @Column(name = "payment_no")
    private String paymentNo;
    
    @Column(name = "supplier")
    private String supplier;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy ="order",fetch=FetchType.LAZY)
    private List<OrderDetail> orderDetails;
    
    @PrePersist
    public void onCreate() {
    	if(createDate==null) {
    		createDate=new Date();
    	}
    	if(modifyDate==null) {
    		modifyDate=new Date();
    	}
    }
 
	@Override
	public String toString() {
		return "Order [id=" + id + ", user_id=" + user.getId() + ", createDate=" + createDate + ", modifyDate=" + modifyDate
				+ ", orderAmount=" + orderAmount + ", movie=" + movie.getName() + ", paymentCondition=" + paymentCondition+ ", orderStatus=" + orderStatus+"]";
	}

	@Override
	public boolean isNew() {
		return true;
	}
	
	
}