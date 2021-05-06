package com.sixpoints.card.controller;

import com.sixpoints.card.service.CardServiceToWeb;
import com.sixpoints.dynasty.service.DynastyServiceToWeb;
import com.sixpoints.entity.card.Card;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RequestMapping("/web/card")
@Controller
public class CardControllerToWeb {
    @Resource
    private CardServiceToWeb cardServiceToWeb;

    @Resource
    private DynastyServiceToWeb dynastyServiceToWeb;

    @RequestMapping("/list/{dynastyId}")
    public String getCardList(@PathVariable("dynastyId")int dynastyId, HttpSession session){
        session.setAttribute("cardList",cardServiceToWeb.getCardsByDynasty(dynastyId));
        session.setAttribute("cardDynasty",dynastyId);
        return "redirect:/ui-panels.html";
    }

    @RequestMapping("/list")
    public String getDynastyList(HttpSession session){
        if(session.getAttribute("dynastyList") != null){
            session.setAttribute("dynastyList",dynastyServiceToWeb.getDynastyList());
        }
        session.setAttribute("cardDynasty",1);
        return "redirect:/ui-panels.html";
    }

    @RequestMapping("/add")
    public String addCard(@RequestParam("cardName")String cardName,
                          @RequestParam("cardType")int cardType,
                          @RequestParam("cardInfo")String cardInfo,
                          @RequestParam("cardStory")String cardStory,
                          @RequestParam("cardCreator")String cardCreator,
                          @RequestParam("file") MultipartFile cardFile
                          ,HttpSession session){
        Card card = new Card();
        card.setCardName(cardName);
        card.setCardType(cardType);
        card.setCardInfo(cardInfo);
        card.setCardStory(cardStory);
        card.setCardCreator(cardCreator);

        int dynastyId = (int) session.getAttribute("cardDynasty");
        //判断
        if(card.getCardType()!=null&&!"".equals(card.getCardType())&&
                card.getCardName()!=null&&!"".equals(card.getCardName())&&
                card.getCardCreator()!=null&&!"".equals(card.getCardCreator())){
            //保存
            cardServiceToWeb.addCard(card, dynastyId,cardFile);
        }
        session.setAttribute("cardList",cardServiceToWeb.getCardsByDynasty(dynastyId));
        session.setAttribute("cardDynasty",dynastyId);
        return "redirect:/ui-panels.html";
    }

    //跳转修改卡片页面
    @RequestMapping("/jump/{cardId}")
    public String jumpModifyCard(@PathVariable("cardId")int cardId,HttpSession session){
        //查询卡片详情
        Card cardDetails = cardServiceToWeb.getCardDetails(cardId);
        session.setAttribute("modifyCard",cardDetails);
        return "redirect:/modify-card.html";
    }

    @RequestMapping("/modify")
    public String modifyCard(@RequestParam("cardId")int cardId,
                             @RequestParam("cardName")String cardName,
                             @RequestParam("cardType")int cardType,
                             @RequestParam("cardInfo")String cardInfo,
                             @RequestParam("cardStory")String cardStory,
                             @RequestParam("cardCreator")String cardCreator,
                             @RequestParam("file") MultipartFile cardFile
                             ,HttpSession session){
        Card card = new Card();
        card.setCardId(cardId);
        card.setCardName(cardName);
        card.setCardType(cardType);
        card.setCardInfo(cardInfo);
        card.setCardStory(cardStory);
        card.setCardCreator(cardCreator);

        int dynastyId = (int) session.getAttribute("cardDynasty");
        //判断
        if(card.getCardType()!=null&&!"".equals(card.getCardType())&&
                card.getCardName()!=null&&!"".equals(card.getCardName())&&
                card.getCardCreator()!=null&&!"".equals(card.getCardCreator())){
            //保存
            cardServiceToWeb.addCard(card, dynastyId,cardFile);
        }
        session.setAttribute("cardList",cardServiceToWeb.getCardsByDynasty(dynastyId));
        session.setAttribute("cardDynasty",dynastyId);
        return "redirect:/ui-panels.html";
    }
}
