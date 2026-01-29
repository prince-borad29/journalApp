package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendMail(){
        emailService.sendMail("pborad112@rku.ac.in","testing java mail sender","Hello I Am The Mail");
    }

}
