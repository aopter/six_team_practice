package com.sixpoints.card.dao;

import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.dynasty.Dynasty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardDao extends JpaRepository<Card,Integer>, JpaSpecificationExecutor<Card> {

    List<Card> findCardsByCardDynasty(Dynasty dynasty);

    @Query(value="select * from ca_card where dynasty_id=?1 order by card_type,card_creation_time desc",nativeQuery=true)
    List<Card> findCards(int dynastyId);

}
