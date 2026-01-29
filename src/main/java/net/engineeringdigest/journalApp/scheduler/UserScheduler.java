package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private AppCache appCache;

//    @Autowired
//    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    //    @Scheduled(cron = "0 0 9 ? * SUN")
    public void fetchUserAndSendSaMail() {
        List<Users> users = userRepositoryImpl.getUserForSA();

        for (Users user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments =
                    journalEntries
                            .stream()
                            .filter(x -> x.getDate()
                                    .isAfter(LocalDateTime.now()
                                            .minus(7, ChronoUnit.DAYS))
                            )
                            .map(x -> x.getSentiment())
                            .collect(Collectors.toList());


            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }

            Sentiment mostFrequentSentiment = null;

            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {

//                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment For Last 7 Days" + mostFrequentSentiment).build();
                emailService.sendMail(user.getEmail(), "Sentiment Data For Last 7 Days" , mostFrequentSentiment.toString());
//                kafkaTemplate.send("weekly-sa", sentimentData.getEmail(), sentimentData);
            }

        }

    }

    //    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void clearAppCache() {
        appCache.init();
    }
}
