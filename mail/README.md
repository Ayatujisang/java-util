
* [python发邮件](https://github.com/kongzhidea/wiki/tree/master/python/mail)
```
如果多个账号：fromAddress 来发送，那么不要用
Session.getDefaultInstance，  会从缓存中取。
应该用

Session.getInstance，    每次都生成的新的

pom:
<!-- mail -->
<dependency>
   <groupId>javax.mail</groupId>
   <artifactId>mail</artifactId>
   <version>1.4</version>
</dependency>



package com.kk.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class SendMailUtil {

    private final static String DEFAULT_MAIL_SERVER = "smtp.exmail.qq.com";// 腾讯企业邮箱

    public static void main(String[] args) {
    }

    // file 可以是null，则发 普通邮件
    // 群发时候注意，如果有1个邮箱配置错误，需要验证其他人是否可以收到
    public static boolean sendHtmlMailWithAttachment(String fromAddress, String toAddress, String password, String subject, String content, File attachment) {
        Properties pro = getProperties();
        MyAuthenticator authenticator = new MyAuthenticator(fromAddress, password);
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            String[] split = toAddress.split(",");
            for (String s : split) {
                Address to = new InternetAddress(s);
                mailMessage.addRecipient(Message.RecipientType.TO, to);
                System.out.println("create receiver: " + s);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(subject);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));

                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");

                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                mainPart.addBodyPart(attachmentBodyPart);
            }

            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 保存邮件
            mailMessage.saveChanges();
            // 发送邮件
            System.out.println("send mail: " + subject);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    // in可以是null
    // 群发时候注意，如果有1个邮箱配置错误，需要验证其他人是否可以收到
    public static boolean sendHtmlMailWithAttachment2(String fromAddress, String toAddress, String password, String subject, String content, InputStream in, String attachmentName) {
        Properties pro = getProperties();
        MyAuthenticator authenticator = new MyAuthenticator(fromAddress, password);
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            String[] split = toAddress.split(",");
            for (String s : split) {
                Address to = new InternetAddress(s);
                mailMessage.addRecipient(Message.RecipientType.TO, to);
                System.out.println("create receiver: " + s);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(subject);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            if (in != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(in, "application/octet-stream");
                attachmentBodyPart.setDataHandler(new DataHandler(source));

                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");

                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachmentName));
                mainPart.addBodyPart(attachmentBodyPart);
            }

            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 保存邮件
            mailMessage.saveChanges();
            // 发送邮件
            System.out.println("send mail: " + subject);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送html邮件
     *
     * @param fromAddress 发件人.
     *                    只需要传入邮箱前缀
     * @param toAddress   收件人.
     *                    只需要传入邮箱前缀. 多个收件人使用英文逗号分隔. 不需要空格
     * @param password    发件人密码
     * @param subject     邮件主题
     * @param content     html格式的邮件正文
     * @return 是否发送成功
     */
    public static boolean sendHtmlMail(String fromAddress, String toAddress, String password, String subject, String content) {
        Properties pro = getProperties();
        MyAuthenticator authenticator = new MyAuthenticator(fromAddress, password);
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            String[] split = toAddress.split(",");
            for (String s : split) {
                Address to = new InternetAddress(s);
                mailMessage.addRecipient(Message.RecipientType.TO, to);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(subject);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", DEFAULT_MAIL_SERVER); // 腾讯企业邮箱使用smtp即可，不用ssl
        p.put("mail.smtp.port", "25"); // string important!!
        p.put("mail.smtp.auth", "true");
        return p;
    }

    static class MyAuthenticator extends Authenticator {
        String userName = null;
        String password = null;

        public MyAuthenticator(String username, String password) {
            this.userName = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }
    }
}


/**
// smtps 基于ssl
Properties p = new Properties();
p.put("mail.smtp.host", DEFAULT_MAIL_SERVER);
p.put("mail.smtp.port", "465"); // smtps
p.put("mail.smtp.sendpartial", "true" );
p.put("mail.smtp.auth", "true");
p.put("mail.smtp.socketFactory.port", "465"); // smtps
p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

*/

```
