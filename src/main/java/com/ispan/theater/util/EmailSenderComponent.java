package com.ispan.theater.util;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderComponent {
	

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    @Value("${mail.smtp.username}")
    private String smtpUsername;

    @Value("${mail.smtp.password}")
    private String smtpPassword;

    @Value("${mail.smtp.tls}")
    private boolean smtpTLS;
    
    @Value("${front.url}")
    private String frontUrl;
    
    public void sendEmail(String useremail,String token) {

        // 連接郵件服務器
        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, smtpPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        

        // 建構郵件模板
        Email email = EmailBuilder.startingBlank()
                .from(smtpUsername)
                .to(useremail)
                .withSubject("星爆影城認證信")
                .withHTMLText("<html><body>" +
                        "<p>敬愛的客戶您好，歡迎註冊星爆影城會員:</p>" +
                        "<a href=\"" + frontUrl+"/verify-email/" + token + "\">點擊進行email認證</a>" +
                        "</body></html>")
                .buildEmail();

        // 發送郵件
        mailer.sendMail(email,/* async = */true);
    	
    }
    
    public void sendForgetPasswordEmail(String useremail,String token) {
        // 配置邮件服务器
        // 連接郵件服務器
        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, smtpPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        // 建構郵件模板
        Email email = EmailBuilder.startingBlank()
                .from(smtpUsername)
                .to(useremail)
                .withSubject("星爆影城:忘記密碼重製信")
                .withHTMLText("<html><body>" +
                        "<p>敬愛的客戶您好:</p>" +
                        "<a href=\"" + frontUrl+"/reset-password/" + token + "\">點擊進行密碼重置</a>" +
                        "</body></html>")
//                .withPlainText("請點擊以下連接進行忘記密碼重製：http://localhost:5173/reset-password/" + token)
                .buildEmail();

        // 發送郵件
        mailer.sendMail(email,/* async = */true);
    	
    }
    
    public void sendCSEmail(String useremail) {

        // 連接郵件服務器
        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, smtpPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        

        // 建構郵件模板
        Email email = EmailBuilder.startingBlank()
                .from(smtpUsername)
                .to(useremail)
                .withSubject("星爆影城--已收到您的投訴 - 我們正在處理中")
                .withHTMLText("<html><body>" +
                        "<p>親愛的貴賓，</p>" +
                        "<p>您好！</p>" +
                        "<p>感謝您聯繫我們並反映問題。我們已經收到您提交的反映，並正在積極處理中。</p>\r\n"+
                        "<p>我們非常重視您的反饋，並會在例如72小時內與您聯繫，提供進一步的更新和解決方案。</p>\r\n" +
                        "<p>在此期間，若您有任何其他問題或需要更多信息，請隨時通過以下方式與我們聯繫：</p>\r\n" +
                        "<p>電話：02-20202032</p>\r\n" +
                        "<p>感謝您的耐心等待。</p>\r\n" +
                        "<p>此致</p>\r\n" +
                        "<p>敬禮</p>\r\n" +
                        "<p>星爆影城 客戶服務團隊</p>\r\n" +
                        "</body></html>")
                .buildEmail();

        // 發送郵件
        mailer.sendMail(email,/* async = */true);
    	
    }
    

}
