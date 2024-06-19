package com.ispan.theater.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Food_OrderDetail")
public class FoodOrderDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "foodOrderDetail_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "foodOrder_id", nullable = false)
	private FoodOrder Order;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "food_id", nullable = false)
	private Food food;
	
	@Column(name = "buy_number", nullable = false)
	private Integer buyNumber;
	
	@Column(name = "total_price", nullable = false)
    private Integer totalPrice;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

	@Override
	public String toString() {
		return "FoodOrderDetail [id=" + id + ", Order=" + Order + ", food=" + food + ", buyNumber=" + buyNumber
				+ ", totalPrice=" + totalPrice + ", createDate=" + createDate + ", modifyDate=" + modifyDate + "]";
	}

    

}
