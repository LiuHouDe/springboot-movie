package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Customer_Service")
public class CustomerService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_service_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "text", nullable = false, length = 500)
    private String text;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "user_email", length = 50)
    private String userEmail;

    @Column(name = "status", nullable = false, length = 20)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_emp_id")
    private Employee agentEmp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "handle_date")
    private Date handleDate;

	@Override
	public String toString() {
		return "CustomerService [id=" + id + ", user=" + user + ", text=" + text + ", category=" + category
				+ ", userEmail=" + userEmail + ", status=" + status + "]";
	}

}