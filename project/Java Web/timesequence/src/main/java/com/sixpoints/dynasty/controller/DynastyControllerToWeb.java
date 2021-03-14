package com.sixpoints.dynasty.controller;

import com.sixpoints.dynasty.service.DynastyServiceToWeb;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.DynastyListVO;
import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.incident.service.IncidentServiceToWeb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/web/dynasty")
public class DynastyControllerToWeb {
    @Resource
    private DynastyServiceToWeb dynastyServiceToWeb;

    @Resource
    private IncidentServiceToWeb incidentServiceToWeb;

    @RequestMapping("/list/{dynastyId}/{pageNum}")
    public String getDynastyList(@PathVariable("dynastyId")int dynastyId, @PathVariable("pageNum")int pageNum, HttpSession session){
        //获取事件信息
        List<Incident> incidentList = incidentServiceToWeb.getIncidentList(dynastyId, pageNum, 20);
        session.setAttribute("incidentList",incidentList);
        //添加朝代标识符
        session.setAttribute("flag",dynastyId);
        //添加共多少个事件
        session.setAttribute("total",incidentList.size());
        //添加现在第几个事件到第几个事件
        session.setAttribute("start",pageNum);
        session.setAttribute("end",pageNum+incidentList.size()-1);
        //页面跳转
        return "redirect:/mail-inbox.html";
    }

    @RequestMapping("/list")
    public String getDynastyList(HttpSession session){
        List<DynastyListVO> dynastyList = dynastyServiceToWeb.getDynastyList();
        session.setAttribute("dynastyList",dynastyList);
        //获取事件信息
        List<Incident> incidentList = incidentServiceToWeb.getIncidentList(dynastyList.get(0).getDynastyId(), 1, 20);
        session.setAttribute("incidentList",incidentList);
        //添加朝代标识符
        session.setAttribute("flag",dynastyList.get(0).getDynastyId());
        //添加共多少个事件
        session.setAttribute("total",incidentList.size());
        //添加现在第几个事件到第几个事件
        session.setAttribute("start",1);
        session.setAttribute("end",incidentList.size());
        //返回数据
        return "redirect:/mail-inbox.html";
    }

    @RequestMapping("/adddynasty")
    public String addDynasty(Dynasty dynasty, HttpSession session){
        if(dynasty.getDynastyName()!=null&&!"".equals(dynasty.getDynastyName())&&
        dynasty.getDynastyCreator()!=null&&!"".equals(dynasty.getDynastyCreator())){
            boolean b = dynastyServiceToWeb.createDynasty(dynasty);
            if(b){
                return getDynastyList(session);
            }
            return "redirect:/mail-compose.html";
        }
        return "redirect:/mail-compose.html";
    }

    @RequestMapping("/details/{dynastyId}")
    public String getDetails(@PathVariable("dynastyId")int dynastyId,HttpSession session){
        session.setAttribute("details",dynastyServiceToWeb.getDynastyDetails(dynastyId));
        return "redirect:/modify-dynasty.html";
    }

    @RequestMapping("/modfiy")
    public String modifyDynasty(Dynasty dynasty,HttpSession session){
        if(dynasty.getDynastyName()!=null&&!"".equals(dynasty.getDynastyName())&&
                dynasty.getDynastyCreator()!=null&&!"".equals(dynasty.getDynastyCreator())){
            boolean b = dynastyServiceToWeb.modifyDynasty(dynasty);
            if(b){
                return getDynastyList(session);
            }
            return "redirect:/modify-dynasty.html";
        }
        return "redirect:/mail-compose.html";
    }

}
