package cn.wolfcode.p2p.base.vo;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by seemygo on 2017/12/15.
 */
public class URLTest {
    public static void main(String[] args) throws Exception {
        //定义请求的地址
        URL url = new URL("https://way.jd.com/turing/turing");
        //创建连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置请求的方式
        urlConnection.setRequestMethod("POST");
        //设置要输出参数
        urlConnection.setDoOutput(true);
        //给地址添加参数
        StringBuilder msg = new StringBuilder(100);
        msg.append("info=").append("今晚打老虎.")
                .append("&loc=").append("广州天河区")
                .append("&userid=").append("222")
                .append("&appkey=").append("e50b3303a2fa65774b440c0f084a82b9");
        //写出参数
        urlConnection.getOutputStream().write(msg.toString().getBytes("utf-8"));
        //和服务建立连接
        urlConnection.connect();
        //获取服务响应的内容
        String respStr = StreamUtils.copyToString(urlConnection.getInputStream(), Charset.forName("utf-8"));
        System.out.println(respStr);

    }
}
