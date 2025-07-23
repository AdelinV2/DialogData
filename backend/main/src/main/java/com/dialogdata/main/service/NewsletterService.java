package com.dialogdata.main.service;

import com.dialogdata.main.entity.Newsletter;
import com.dialogdata.main.repository.NewsletterRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableAsync
public class NewsletterService {

    private final UserService userService;
    private final NewsletterRepository newsletterRepository;
    private final EmailService emailService;

    public Newsletter create(Newsletter newsletter) {
        return newsletterRepository.save(newsletter);
    }

    public Newsletter findById(Integer id) {
        return newsletterRepository.findById(id).orElse(null);
    }

    public List<Newsletter> findAll() {
        return newsletterRepository.findAll();
    }

    public Newsletter update(Integer id, Newsletter newsletter) {

        Newsletter existingNewsletter = findById(id);

        if (existingNewsletter == null) {
            return null;
        }

        newsletter.setId(id);

        return newsletterRepository.save(newsletter);
    }

    public boolean delete(Integer id) {

        if (newsletterRepository.existsById(id)) {
            newsletterRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void sendScheduledNewsletters() {

        newsletterRepository.findAll().forEach(newsletter -> {
            if (newsletter.getScheduleDate().isBefore(Instant.now())) {
                if (newsletter.getRepeat()) {
                    newsletter.setScheduleDate(newsletter.getScheduleDate().plusSeconds(newsletter.getRepeatInterval()));
                    newsletterRepository.save(newsletter);
                } else {
                    newsletterRepository.delete(newsletter);
                }

                userService.getAllUsersSubscribed().forEach(user -> {
                    try {
                        emailService.sendHtmlEmail(user.getEmail(), "Newsletter", newsletter.getContent());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });

                System.out.println("Newsletter " + newsletter + "sent");
            }
        });
    }


}
