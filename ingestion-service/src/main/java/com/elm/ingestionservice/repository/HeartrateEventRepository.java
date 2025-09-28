    package com.elm.ingestionservice.repository;
    
    import com.elm.ingestionservice.entity.HeartrateEventEntity;
    import org.springframework.data.jpa.repository.JpaRepository;


    public interface HeartrateEventRepository extends JpaRepository<HeartrateEventEntity, Long> {
    }
