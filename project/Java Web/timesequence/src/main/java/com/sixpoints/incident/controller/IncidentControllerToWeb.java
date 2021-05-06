package com.sixpoints.incident.controller;

import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.incident.service.IncidentServiceToWeb;
import com.sixpoints.utils.PathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/incident")
public class IncidentControllerToWeb {
    @Resource
    private IncidentServiceToWeb incidentServiceToWeb;

    @RequestMapping("/details/{incidentId}")
    public String getIncidentDetails(@PathVariable("incidentId")int incidentId, HttpSession session){
        Incident incidentById = incidentServiceToWeb.getIncidentById(incidentId);
        session.setAttribute("incident",incidentById);
        if(session.getAttribute("path") == null){
            session.setAttribute("path", (PathUtil.getRootPath()+"\\img\\").replace("\\","/"));
        }
        return "redirect:/mail-view.html";
    }

    @RequestMapping("/modify")
    public String modifyIncident(@RequestParam("incidentId") Integer incidentId,
                                 @RequestParam("incidentName") String incidentName,
                                 @RequestParam("incidentCreator") String incidentCreator,
                                 @RequestParam("incidentInfo") String incidentInfo,
                                 @RequestParam("incidentDialog") String incidentDialog,
                                 @RequestParam("incident") MultipartFile fileIncident,
                                 @RequestParam("other")MultipartFile fileOther,
                                 @RequestParam("fileOne")MultipartFile fileOne,
                                 @RequestParam("fileTwo")MultipartFile fileTwo,HttpSession session){
        Incident tempIncident = new Incident();
        tempIncident.setIncidentId(incidentId);
        tempIncident.setIncidentName(incidentName);
        tempIncident.setIncidentCreator(incidentCreator);
        tempIncident.setIncidentInfo(incidentInfo);
        tempIncident.setIncidentDialog(incidentDialog);
        //创建集合
        List<MultipartFile> list = new ArrayList<>();
        list.add(fileIncident);
        list.add(fileOther);
        list.add(fileOne);
        list.add(fileTwo);
        //判空
        if(tempIncident.getIncidentId()!= null&&!"".equals(tempIncident.getIncidentId())&&
            tempIncident.getIncidentName()!=null&&!"".equals(tempIncident.getIncidentName())&&
            tempIncident.getIncidentCreator()!=null&&!"".equals(tempIncident.getIncidentCreator())){
            //进行保存
            incidentServiceToWeb.modifyIncident(tempIncident,list);
            //保存到session中
            Incident incidentById = incidentServiceToWeb.getIncidentById(tempIncident.getIncidentId());
            session.setAttribute("incident",incidentById);
            //跳回详情页面
            return "redirect:/mail-view.html";
        }
        //返回修改页面
        return "redirect:/modify-incident.html";

    }

    @RequestMapping("/add")
    public String addIncidnet(@RequestParam("incidentName") String incidentName,
                              @RequestParam("incidentCreator") String incidentCreator,
                              @RequestParam("incidentInfo") String incidentInfo,
                              @RequestParam("incidentDialog") String incidentDialog,
                              @RequestParam("incident") MultipartFile fileIncident,
                              @RequestParam("other")MultipartFile fileOther,
                              @RequestParam("fileOne")MultipartFile fileOne,
                              @RequestParam("fileTwo")MultipartFile fileTwo,HttpSession session){
        Incident incident = new Incident();
        incident.setIncidentName(incidentName);
        incident.setIncidentCreator(incidentCreator);
        incident.setIncidentInfo(incidentInfo);
        incident.setIncidentDialog(incidentDialog);
        //判空
        if(incident.getIncidentName()!=null&&!"".equals(incident.getIncidentName())&&
                incident.getIncidentCreator()!=null&&!"".equals(incident.getIncidentCreator())){
            //创建集合
            List<MultipartFile> list = new ArrayList<>();
            list.add(fileIncident);
            list.add(fileOther);
            list.add(fileOne);
            list.add(fileTwo);
            //添加
            boolean flag = incidentServiceToWeb.addIncident(incident, (Integer) session.getAttribute("flag"),list);
            //判断
            if(flag){
                //添加成功
                //获取事件信息
                List<Incident> incidentList = incidentServiceToWeb.getIncidentList((Integer) session.getAttribute("flag"), 1, 20);
                session.setAttribute("incidentList",incidentList);
                //添加朝代标识符
                session.setAttribute("flag",session.getAttribute("flag"));
                //添加共多少个事件
                session.setAttribute("total",incidentList.size());
                //添加现在第几个事件到第几个事件
                session.setAttribute("start",1);
                session.setAttribute("end",1+incidentList.size()-1);
                //页面跳转
                return "redirect:/mail-inbox.html";
            }else{
                //添加失败
                return "redirect:/add-incident.html";
            }

        }
        return "redirect:/add-incident.html";
    }
}
