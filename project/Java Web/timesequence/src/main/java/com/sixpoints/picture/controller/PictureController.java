package com.sixpoints.picture.controller;

import com.sixpoints.picture.service.PictureService;
import com.sixpoints.utils.PathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @RequestMapping("/download/{pictureType}/{picturePath}")
    public void downloadPicture(HttpServletResponse response, @PathVariable("picturePath")String picturePath,@PathVariable("pictureType")String pictureType){
        //获取根路径
        String rootPath = PathUtil.getRootPath();
        //获取路径
        String path = (rootPath+"\\img\\"+pictureType+"\\"+picturePath).replace("\\","/");
        System.out.println(path);
        //判断图片是否存在
        File file = new File(path);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        System.out.printf("查询图片中");
        if(file.exists()){
            System.out.println("图片存在！！！\n 下载中，请稍等***************");
            try {
                //存在，则读取图片（获取图片输入流）
                inputStream = new FileInputStream(path);
                //获取网络输出流
                outputStream = response.getOutputStream();
                //循环读写（输出图片）
                int b = -1;
                while ((b = inputStream.read()) != -1){
                    outputStream.write(b);
                    outputStream.flush();
                }
                System.out.println("*****下载完成*****");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            try {
                response.getWriter().println("资源没有找到！");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.printf("图片不存在");
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadPicture(@RequestParam("file")MultipartFile file,@RequestParam("userId")int userId){
        System.out.println("*****图片上传中*****");
        return "{'result':"+ pictureService.headerPictureUpload(userId,file) +"}";
    }

    @RequestMapping("/path")
    @ResponseBody
    public String getPath(){
        return PathUtil.getRootPath();
    }
}
