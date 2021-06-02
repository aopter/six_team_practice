package com.sixpoints.usercard.controller;

import com.sixpoints.entity.card.CardListVO;
import com.sixpoints.entity.user.card.UserCardVO;
import com.sixpoints.usercard.dao.UserCardDao;
import com.sixpoints.usercard.service.UserCardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/usercard")
public class UserCardController {
    @Resource
    private UserCardService userCardService;

    @RequestMapping("/list/{userId}/{dynastyId}/{cardType}")
    @ResponseBody
    public List<UserCardVO> queryExistingCard(@PathVariable("userId") int userId, @PathVariable("dynastyId") int dynastyId, @PathVariable("cardType") int cardType) {
        if (cardType == 0) {
            return userCardService.queryExistingCardByIdAndDynasty(userId, dynastyId);
        }
        return userCardService.queryExistingCardByIdAndDynasty(userId, dynastyId, cardType);
    }

    @RequestMapping("/list/{userId}/{dynastyId}/{cardType}/{key}")
    @ResponseBody
    public List<UserCardVO> fuzzySearch(@PathVariable("userId") int userId, @PathVariable("dynastyId") int dynastyId, @PathVariable("cardType") int cardType, @PathVariable("key") String key) {
        if (cardType == 0) {
            return userCardService.fuzzySearch(userId, dynastyId, key);
        }
        return userCardService.fuzzySearch(userId, dynastyId, cardType, key);
    }

    @RequestMapping("/all/{userId}")
    @ResponseBody
    public List<UserCardVO> queryAllExistingCards(@PathVariable("userId") int userId) {
        return userCardService.queryExistingCardByUserId(userId);
    }

    @RequestMapping("/not/{userId}/{dynastyId}")
    @ResponseBody
    public List<CardListVO> queryNotExistingCards(@PathVariable("userId") int userId, @PathVariable("dynastyId") int dynastyId) {
        return userCardService.queryNotExistingCardByIdAndDynasty(userId, dynastyId);
    }

    @RequestMapping("/add/{userId}/{cardId}")
    @ResponseBody
    public String canDraw(@PathVariable("userId") int userId, @PathVariable("cardId") int cardId) {
        return userCardService.addCard(userId, cardId) ? "{'result':true}" : "{'result':false}";
    }

}
