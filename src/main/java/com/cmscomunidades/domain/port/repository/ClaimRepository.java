package com.cmscomunidades.domain.port.repository;

import com.cmscomunidades.infrastructure.adapter.repository.ClaimBD;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@Repository
public interface ClaimRepository extends JpaRepository<ClaimBD, Long>, QuerydslPredicateExecutor<ClaimBD> ,JpaSpecificationExecutor<ClaimBD> {

    @Query("SELECT c FROM ClaimBD c WHERE c.ebroker_id = (SELECT MAX(c2.ebroker_id) FROM ClaimBD c2)")
    Optional<ClaimBD> findTopByOrderByIdDesc();
}
