package com.elm.ingestionservice.repository;

import com.elm.ingestionservice.entity.StepEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StepEventRepository extends JpaRepository<StepEventEntity, Long> {
}
