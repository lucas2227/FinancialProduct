package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.DateUtil;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.base.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * Created by seemygo on 2017/12/15.
 */
@Service@Transactional
public class VerifyCodeServiceImpl implements IVerifyCodeService {
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.username}")
    private String username;
    @Value("${sms.password}")
    private String password;
    @Value("${sms.apiKey}")
    private String apiKey;
    @Override
    public void sendVerifyCode(String phoneNumber)  {
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        //什么条件才能发验证码?
        //1.之前没有发过验证码?
        //2.之前发过验证码,已经超过有效时间了(90s)
        if(vo==null || DateUtil.getBetweenTime(vo.getSendTime(),new Date())>BidConst.MESSAGER_INTERVAL_TIME){
            //1.生成验证码
            String verifyCode = UUID.randomUUID().toString().substring(0,4);
            //2.执行发送短信的代码
            StringBuilder msg = new StringBuilder(50);
            msg.append("您的验证码为:").append(verifyCode).append(",有效时间为:").append(BidConst.MESSAGER_VAILD_TIME).append("分钟,请尽快使用.");
            //模拟发送
            try {
                sendRealMessage(phoneNumber,msg.toString());
                //System.out.println(msg);
                //3.把手机号码,验证码,发送时间放入到session中.
                vo = new VerifyCodeVo();
                vo.setPhoneNumber(phoneNumber);
                vo.setSendTime(new Date());
                vo.setVerifyCode(verifyCode);
                UserContext.setVerifyCodeVo(vo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new RuntimeException("发送太频繁了,请稍后再试.");
        }
    }

    @Override
    public boolean validate(String phoneNumber, String verifyCode) {
        //1.验证传入的手机和发送的手机号是否一致,验证码一致,发送的时间和当前时间的间隔没有超过短信的有效时间
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        if(vo!=null && vo.getPhoneNumber().equals(phoneNumber)&&
                vo.getVerifyCode().equalsIgnoreCase(verifyCode)&&
                DateUtil.getBetweenTime(vo.getSendTime(),new Date())<=BidConst.MESSAGER_VAILD_TIME*60
                ){
            return true;
        }
        return false;
    }
    private void sendRealMessage(String phoneNumber,String content) throws Exception {
        //定义请求的地址
        URL url = new URL("http://utf8.api.smschinese.cn");
        //创建连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置请求的方式
        urlConnection.setRequestMethod("POST");
        //设置要输出参数
        urlConnection.setDoOutput(true);
        //给地址添加参数
        StringBuilder msg = new StringBuilder(100);
        msg.append("Uid=").append("lanxw02")
                .append("&Key=").append("f674f701346030e3af88")
                .append("&smsMob=").append(phoneNumber)
                .append("&smsText=").append(content);
        //写出参数
        urlConnection.getOutputStream().write(msg.toString().getBytes("utf-8"));
        //和服务建立连接
        urlConnection.connect();
        //获取服务响应的内容
        String respStr = StreamUtils.copyToString(urlConnection.getInputStream(), Charset.forName("utf-8"));
        int code = Integer.parseInt(respStr);
        if(code<0){
            throw new RuntimeException("短信调用失败,请联系管理员");
        }
    }
    private void sendMessage(String phoneNumber,String content) throws Exception {
        //定义请求的地址
        URL url = new URL(smsUrl);
        //创建连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置请求的方式
        urlConnection.setRequestMethod("POST");
        //设置要输出参数
        urlConnection.setDoOutput(true);
        //给地址添加参数
        StringBuilder msg = new StringBuilder(100);
        msg.append("username=").append(username)
                .append("&password=").append(password)
                .append("&apiKey=").append(apiKey)
                .append("&phoneNumber=").append(phoneNumber)
                .append("&content=").append(content);
        //写出参数
        urlConnection.getOutputStream().write(msg.toString().getBytes("utf-8"));
        //和服务建立连接
        urlConnection.connect();
        //获取服务响应的内容
        String respStr = StreamUtils.copyToString(urlConnection.getInputStream(), Charset.forName("utf-8"));
        if(!"success".equalsIgnoreCase(respStr)){
            throw new RuntimeException("短信调用失败,请联系管理员");
        }
    }
}
