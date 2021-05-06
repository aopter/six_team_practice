package com.sixpoints.usercard.dao;

import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.user.card.UserCard;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCardDao extends JpaRepository<UserCard,Integer>, JpaSpecificationExecutor<UserCard> {

    Boolean existsUserCardByCard(Card card);

    UserCard findUserCardByCard(Card card);

}
