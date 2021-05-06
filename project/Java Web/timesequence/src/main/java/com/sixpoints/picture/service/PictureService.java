package com.sixpoints.picture.service;

import com.sixpoints.entity.user.User;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.utils.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class PictureService {

    @Resource
    private UserDao userDao;

    public boolean headerPictureUpload(int userId,MultipartFile multipartFile){
        Optional<User> userOptional = userDao.findById(userId);
        if(!userOptional.isPresent()){
            return false;
        }
        if(multipartFile.isEmpty()){
            return false;
        }
        String rootPath = PathUtil.getRootPath();
        String filePath = "\\user\\us-"+userId+".jpg";
        String path = (rootPath+"\\img"+filePath).replace("\\","/");

        File file = new File(path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }

        //拷贝文件
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //存入数据库
        userOptional.get().setUserHeader("user/us-"+userId+".jpg");
        userDao.save(userOptional.get());
        System.out.printf("******头像上传成功******");
        return true;
    }

    public boolean pictureUpload(String name,MultipartFile multipartFile){
        if(multipartFile.isEmpty()){
            return false;
        }
        String rootPath = PathUtil.getRootPath();
        String path = (rootPath+"\\img\\"+name).replace("\\","/");

        File file = new File(path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }

        //拷贝文件
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
