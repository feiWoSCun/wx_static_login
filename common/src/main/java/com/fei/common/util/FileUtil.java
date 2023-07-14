package com.fei.common.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */
public class FileUtil {


    public static void getStaticFile(String fileName) throws UnsupportedEncodingException {
        HttpServletResponse response = ServletUtil.getResponse();
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            String path = Thread.currentThread().getContextClassLoader().getResource("static").getPath();
            InputStream stream = new FileInputStream(path+ File.separator +fileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = stream.read(buff)) > 0) {
                out.write(buff, 0, length);
            }
            stream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
