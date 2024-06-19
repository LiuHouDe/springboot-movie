package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ispan.theater.dao.CustomerServiceDao;
import com.ispan.theater.domain.CustomerService;
import com.ispan.theater.domain.Screening;

@Repository
public interface CustomerServiceRepository extends JpaRepository<CustomerService, Integer> ,CustomerServiceDao,JpaSpecificationExecutor<CustomerService>{

    @Query("select count(*) from CustomerService where category = :category")
    public long countByCategory(String category);

    @Query(" from CustomerService where userEmail = :userEmail  order by handleDate desc ")
    public List<CustomerService> findByUserEmail(String userEmail);

    @Query(" from CustomerService where userEmail = :userEmail and id = :id order by handleDate desc ")
    public List<CustomerService> findByUserEmailAndId(String userEmail, Integer id);
}