package com.ispan.theater.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Food_Picture")
public class FoodPicture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="food_pictureid", nullable=false)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinColumn(name="food_id", nullable = false)
	private Food food;
	
	@JsonIgnore
	@Lob
	@Column(name="food_picture", nullable = false, columnDefinition = "VARBINARY(MAX)")
	private byte[] picture;
	
	@Column(name = "filename", nullable = false)
    private String filename;

	@Override
	public String toString() {
		return "FoodPicture [id=" + id + ", food=" + food + ", picture=" + Arrays.toString(picture) + ", filename="
				+ filename + "]";
	}

	
	
	

	
}
