
```
spring-mail

依赖：

<!-- mail -->
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4</version>
</dependency>
    
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
    <version>4.2.8.RELEASE</version>
</dependency>



application.xml：
host   腾讯企业邮箱：smtp.exmail.qq.com
port   默认25


<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
    <property name="host" value="${mail.host}"/>
    <property name="port" value="25"/>
    <property name="defaultEncoding" value="utf-8"/>
    <property name="username" value="${mail.username}"/>
    <property name="password" value="${mail.password}"/>

    <property name="javaMailProperties">
        <props>
            <prop key="mail.smtp.auth">true</prop>
        </props>
    </property>

</bean>

<bean class="com.kk.utils.MailUtil">
    <property name="mailSender" ref="mailSender"/>
</bean>
   

application.xml中 配置ssl：

<bean id="mailSender_https" class="org.springframework.mail.javamail.JavaMailSenderImpl">
    <property name="host" value="${mail.host}"/>
    <property name="port" value="465"/>
    <property name="defaultEncoding" value="utf-8"/>
    <property name="username" value="${mail.username}"/>
    <property name="password" value="${mail.password}"/>
    <property name="javaMailProperties">
        <props>
            <prop key="mail.smtp.sendpartial">true</prop>
            <prop key="mail.smtp.auth">true</prop>
            <prop key="mail.smtp.socketFactory.port">465</prop>
            <prop key="mail.smtp.socketFactory.class">javax.net.ssl.SSLSocketFactory</prop>
        </props>
    </property>
</bean>
       
    
    
JAVA：

package com.kk.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 正文支持html格式
 * 群发时候注意，如果有1个邮箱配置错误，需要验证其他人是否可以收到
 */
public class MailUtil {
    protected static final Log logger = LogFactory.getLog(MailUtil.class);
    private static JavaMailSenderImpl mailSender;

    public static void sendMail(String title, String html, String[] to) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(mailSender.getUsername());
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(html, true);

            mailSender.send(msg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void sendMail(String title, String html, String to) {
        if (to == null || "".equals(to)) {
            return;
        }
        String[] split = to.split(",");
        sendMail(title, html, split);
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        MailUtil.mailSender = mailSender;
    }
}
    

```