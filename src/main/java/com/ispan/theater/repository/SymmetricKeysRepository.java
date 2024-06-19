package com.ispan.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.SymmetricKeys;

@Repository
public interface SymmetricKeysRepository extends JpaRepository<SymmetricKeys, Integer>{
	
	@Query ("SELECT c FROM SymmetricKeys c ORDER BY c.creationDate DESC LIMIT 1")
	 SymmetricKeys findTop();


}
