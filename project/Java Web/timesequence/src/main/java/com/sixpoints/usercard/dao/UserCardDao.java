package com.sixpoints.usercard.dao;

import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.user.card.UserCard;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCardDao extends JpaRepository<UserCard, Integer>, JpaSpecificationExecutor<UserCard> {

    Boolean existsUserCardByCard(Card card);

    UserCard findUserCardByCard(Card card);

    @Query(value = "select * from us_user_card where user_id = ?1 and card_id = ?2", nativeQuery = true)
    Optional<UserCard> findUserCard(int userId, int cardId);
}
