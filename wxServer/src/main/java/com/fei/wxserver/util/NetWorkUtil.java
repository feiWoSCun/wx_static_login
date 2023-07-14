package com.fei.wxserver.util;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 */
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Author: sgw
 * Date 2019/4/5
 * Description:用于发送http请求的工具类(向微信发送http请求，获取access_token)
 **/
public class NetWorkUtil {
    /**
     * 发起HTTPS请求
     *
     * @param reqUrl
     * @param requestMethod 请求方式，传null的话默认是get请求
     * @return 相应字符串
     */
    public String getHttpsResponse(String reqUrl, String requestMethod) {
        URL url;
        InputStream is;
        String result = "";

        try {
            url = new URL(reqUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            TrustManager[] tm = {xtm};
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            //允许输入流，即允许下载
            con.setDoInput(true);

            //在android中必须将此项设置为false，允许输出流，即允许上传
            con.setDoOutput(false);
            //不使用缓冲
            con.setUseCaches(false);
            if (null != requestMethod && !requestMethod.equals("")) {
                //使用指定的方式
                con.setRequestMethod(requestMethod);
            } else {
                //使用get请求
                con.setRequestMethod("GET");
            }
            //获取输入流，此时才真正建立链接
            is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = bufferReader.readLine()) != null) {
                result += inputLine + "\n";
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    X509TrustManager xtm = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }
    };


public  String doPost(String url, String str, String encoding) {
    String body = "";
     try {
         // 创建httpclient对象
         CloseableHttpClient client = HttpClients.createDefault();
         // 创建post方式请求对象
         HttpPost httpPost = new HttpPost(url);
         // 设置参数到请求对象中
         httpPost.setEntity(new StringEntity(str, encoding));
         // 设置header信息
         // 指定报文头【Content-type】、【User-Agent】
         httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
         // 执行请求操作，并拿到结果（同步阻塞）
         CloseableHttpResponse response = client.execute(httpPost);
         // 获取结果实体
         HttpEntity entity = response.getEntity();
         if (entity != null) {
             // 按指定编码转换结果实体为String类型
             body = EntityUtils.toString(entity, encoding);
         }
         EntityUtils.consume(entity);
         // 释放链接
         response.close();
         return body;
     } catch (Exception e1) {
         e1.printStackTrace();
         return "";

     }
 }


}
