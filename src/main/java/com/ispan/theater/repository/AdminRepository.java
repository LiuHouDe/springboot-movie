package com.ispan.theater.repository;

import com.ispan.theater.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query("select a from Admin a where a.username = :name")
    public Admin findByAdminname(@Param("name") String name);
}