package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.MailVerify;
import cn.wolfcode.p2p.base.service.IEmailService;
import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;

/**
 * Created by seemygo on 2017/12/16.
 */
@Service@Transactional
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private IMailVerifyService mailVerifyService;
    @Value("${email.applicationUrl}")
    private String applicationUrl;//http://localhost:8080
    @Value("${spring.mail.username}")
    private String formEmail;
    @Autowired
    private JavaMailSender sender;
    @Override
    public void sendEmail(String email) {
        //1.构造UUID字符串
        String uuid = UUID.randomUUID().toString();
        //2.拼接相关的信息,发送一封邮件.
        StringBuilder msg= new StringBuilder(50);
        msg.append("感谢注册P2P平台,这是您的认证邮件,点击<a href='").append(applicationUrl).append("/bindEmail?key=")
                .append(uuid).append("'>这里</a>完成认证,有效期为").append(BidConst.EMAIL_VAILD_DAY).append("天");
        //模拟邮件发送
        try {

            //3.把当前发送邮件用户,发送的邮件地址,发送的时间,UUID保存到数据库中
            MailVerify mailVerify = new MailVerify();
            mailVerify.setSendTime(new Date());
            mailVerify.setUserid(UserContext.getCurrent().getId());
            mailVerify.setEmail(email);
            mailVerify.setUuid(uuid);
            mailVerifyService.save(mailVerify);

            sendEmail(email,msg.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void sendEmail(String email,String content) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
        //设置收件人
        helper.setTo(email);
        //设置发件人
        helper.setFrom(formEmail);
        //设置标题
        helper.setSubject("这是一封认证邮件");
        //设置文本内容
        helper.setText(content,true);
        //发送邮件
        sender.send(message);
    }
}
