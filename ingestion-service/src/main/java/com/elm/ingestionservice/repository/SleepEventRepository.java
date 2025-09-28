package com.elm.ingestionservice.repository;

import com.elm.ingestionservice.entity.SleepEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SleepEventRepository extends JpaRepository<SleepEventEntity, Long> {
}
