package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to , String subject , String body){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mail = new MimeMessageHelper(message, true, "UTF-8");
            mail.setFrom("no-reply@journalapp.com","Journal App Support");
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body,true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error While sendMail " + e.getMessage());
        }
    }
}
