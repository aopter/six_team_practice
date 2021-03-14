package com.sixpoints.incident.controller;

import com.sixpoints.entity.dynasty.IncidentListVO;
import com.sixpoints.entity.dynasty.IncidentVO;
import com.sixpoints.incident.service.IncidentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/incident")
public class IncidentController {

    @Resource
    private IncidentService incidentService;

    @RequestMapping("/list/{id}")
    @ResponseBody
    public List<IncidentListVO> incidentList(@PathVariable("id")int id){
        return incidentService.getIncidentList(id);
    }

    @RequestMapping("/details/{id}")
    @ResponseBody
    public IncidentVO incidentDetails(@PathVariable("id")int id){
        return incidentService.getIncidentDetailsById(id);
    }
}
