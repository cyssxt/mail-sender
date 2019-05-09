package com.cyssxt.bean;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
import java.util.Properties;

public class SenderHandler {

    public static MimeMessage createMixedMail(Session session,SenderReq req) throws Exception {
        //创建邮件
        MimeMessage message = new MimeMessage(session);

        //设置邮件的基本信息
        MailUserInfo from = req.getFrom();
        MailUserInfo to = req.getTo();
        message.setFrom(new InternetAddress(from.getAddress()));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to.getAddress()));
        message.setSubject(req.getSubject());

        //正文
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(req.getContent(),"text/html;charset=UTF-8");
        //描述关系:正文和附件
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(text);
        String filePath = req.getFilePath();
        if(filePath!=null && "".equals(filePath)) {
            MimeBodyPart file = new MimeBodyPart();
            DataHandler dh2 = new DataHandler(new FileDataSource(filePath));
            file.setDataHandler(dh2);
            file.setFileName(MimeUtility.encodeText(dh2.getName()));
            mimeMultipart.addBodyPart(file);
        }
        message.setContent(mimeMultipart);
        message.saveChanges();
        //返回创建好的的邮件
        return message;
    }

    static void send(SenderReq req) throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.address", "smtp.exmail.qq.com");
        prop.setProperty("mail.smtp.port", "465");
        prop.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.socketFactory.port", "465");
        Session session = Session.getDefaultInstance(prop);
        Transport ts = session.getTransport();
        MailUserInfo from = req.getFrom();
        ts.connect("smtp.exmail.qq.com",from.getAddress(),from.getPassword());
        Message message = createMixedMail(session,req);
        ts.sendMessage(message,message.getAllRecipients());
        ts.close();
    }

    public static void main(String[] args) throws Exception {
        SenderReq req = new SenderReq();
        req.setContent("aaaaa");
        req.setSubject("bbbb");
        req.setTo(new MailUserInfo("cyssxt@163.com","徐涛"));
        req.setFrom(new MailUserInfo("xutao@seanai.cn","xutao","Cyssxt19901231"));
        send(req);
    }
}
