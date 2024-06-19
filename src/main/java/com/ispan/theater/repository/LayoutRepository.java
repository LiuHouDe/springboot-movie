package com.ispan.theater.repository;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Layout;
import com.ispan.theater.domain.LayoutId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LayoutRepository extends JpaRepository<Layout, LayoutId> {
    @Query("select c from Layout c where c.auditorium = :auditorium")
    public List<Layout> findByAuditorium(@Param("auditorium") Auditorium auditorium);
}