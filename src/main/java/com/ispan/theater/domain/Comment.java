package com.ispan.theater.domain;

import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="commentId")
	private Integer commentId;
	@Column(name="content")
	private String content;
	@Column(name="username")
	private String username;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
	private User userId;
	@Column(name="rate")
	private BigDecimal rate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
	private Movie movieId;
	@Column(name="pid")
	private Integer pid;
	@Column(name="target")
	private String target;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name="createtime")
	private LocalDateTime createtime;
	
	@Column(name="useful")
	private Integer useful=0;
	@Column(name="useless")
	private Integer useless=0;
	@Column(name="good")
	private Integer good=0;
	
//	在資料庫不存在而外的添加資源
	@Transient
	private List<Comment>children;
	
	@Transient
	public String getToken(String token) {
		return token;
	}

}
