package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerAnalysisService {

    @Autowired
    private EmailService emailService;

//    @KafkaListener(topics = "weekly-sa")
//    public void consume(SentimentData sentimentData){
//        sendEmail(sentimentData);
//    }

    private void sendEmail(SentimentData sentimentData){
        emailService.sendMail(sentimentData.getEmail(),"Sentiment For Previous Week", sentimentData.getSentiment());
    }

}
