package com.sixpoints.card.controller;

import com.sixpoints.card.service.CardService;
import com.sixpoints.entity.card.CardVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/card")
public class CardController {
    @Resource
    private CardService cardService;

    @RequestMapping("/draw/{userId}")
    @ResponseBody
    public CardVO drawACard(@PathVariable("userId") int userId){
        return cardService.drawACard(userId);
    }

    @RequestMapping("/candraw/{userId}")
    @ResponseBody
    public String canDraw(@PathVariable("userId")int userId){
        return "{'result':"+ cardService.canDraw(userId) +"}";
    }

    @RequestMapping("/details/{cardId}")
    @ResponseBody
    public CardVO cardDetails(@PathVariable("cardId")int cardId){
        return cardService.getOneCard(cardId);
    }
}
