package com.ispan.theater.repository;

import com.ispan.theater.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("select c from Actor  c where c.name like %:name%")
    public List<Actor> findByNameLike(@Param("name") String name);
    @Query("select c from Actor c where c.name = :name ")
    public Actor findByName(@Param("name") String name);
    @Query(value = "SELECT actor_id,name FROM Actor WHERE CONTAINS(name, :searchTerm)", nativeQuery = true)
    List<Map<String,Object>> searchByNameUsingFullText(@Param("searchTerm") String searchTerm);
}