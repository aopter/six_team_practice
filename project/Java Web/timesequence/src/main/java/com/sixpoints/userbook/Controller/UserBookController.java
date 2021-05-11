package com.sixpoints.userbook.Controller;

import com.sixpoints.entity.user.book.SpecificBookCompletedListVO;
import com.sixpoints.entity.user.book.CertificateUserBookListVO;
import com.sixpoints.entity.user.book.UserBookListVO;
import com.sixpoints.userbook.service.UserBookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/userbook")
public class UserBookController {
    @Resource
    private UserBookService userBookService;

    // 获得某本书籍已经捐赠的15条信息
    @RequestMapping("/donated/{bookId}")
    @ResponseBody
    public List<SpecificBookCompletedListVO> getBookDonatedListVO(@PathVariable("bookId") int bookId) {
        return userBookService.getTop15(bookId);
    }

    // 用户正在进行捐赠的图书列表
    @RequestMapping("/donating/{userId}")
    @ResponseBody
    public List<UserBookListVO> getUserOnDonatingPro(@PathVariable("userId") int userId) {
        return userBookService.getOnProcessing(userId);
    }

    // 用户已经捐赠图书的列表信息
    @RequestMapping("/donatedlist/{userId}")
    @ResponseBody
    public List<CertificateUserBookListVO> getUserDonatedPro(@PathVariable("userId") int userId) {
        return userBookService.getUserBookListVO(userId);
    }

    // 用户进行捐卡
    @RequestMapping("/donate/{userId}/{cardId}/{cardNum}/{procesId}")
    @ResponseBody
    public String updateDonateInfo(@PathVariable("userId") int userId, @PathVariable("cardId") int cardId, @PathVariable("cardNum") int cardNum, @PathVariable("procesId") int procesId) {
        return userBookService.donateCard(userId, cardId, cardNum, procesId) ? "{'result':true}" : "{'result':false}";
    }

    @RequestMapping("/addpro/{userId}/{bookId}")
    @ResponseBody
    public String addDonateInfo(@PathVariable("userId") int userId, @PathVariable("bookId") int bookId) {
        return userBookService.addDonate(userId, bookId) ? "{'result':true}" : "{'result':false}";
    }
}
