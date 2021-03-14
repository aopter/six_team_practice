package com.sixpoints.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class PathUtil {
    public static String getRootPath(){
        // 项目的编译文件的根目录
        String path = null;
        try {
            path = URLDecoder.decode(PathUtil.class.getResource("/").getPath(), String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (path.startsWith("file:")) {
            int i = path.indexOf(".jar!");
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
        }
        // 项目所在的目录
        return new File(path).getParentFile().getAbsolutePath();
    }
}
