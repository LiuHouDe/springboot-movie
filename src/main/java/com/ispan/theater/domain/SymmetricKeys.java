package com.ispan.theater.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "symmetric_keys")
public class SymmetricKeys {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_id", nullable = false)
    private Integer id;
	
	@Column(name = "secret_key" , nullable = false)
	private String secretKey;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date",nullable = false)
	private LocalDateTime  creationDate;
	
}
