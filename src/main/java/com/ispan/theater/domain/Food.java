package com.ispan.theater.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
@Table(name="Food")
public class Food {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="food_id", nullable = false)
	private Integer id;
	
	@Column(name="food_name", nullable = false)
	private String name;
	
	@Column(name="food_price", nullable = false)
	private Double price;
	
	@Column(name="food_count", nullable = false)
	private Integer count;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable = false)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modify_date", nullable = false)
	private Date modifyDate;
	
	@OneToMany(mappedBy = "food", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<FoodPicture> foodPicture;

	@Override
	public String toString() {
		return "Food [id=" + id + ", name=" + name + ", price=" + price + ", count=" + count + ", createDate="
				+ createDate + ", modifyDate=" + modifyDate + "]";
	}
	


	
	
	
}
