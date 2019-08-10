package az.cc.feedback;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private EmailCfg emailCfg;

    public FeedbackController(EmailCfg emailCfg){
        this.emailCfg=emailCfg;
    }

    @PostMapping
    public void processFeedback(@RequestBody Feedback feedback ,
                                BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
        {
            throw new ValidationException("Feedback is invalid");
        }

        //Configure Mail Sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());


        //create simple mail included feedback

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("egasimov0@gmail.com");
        mailMessage.setSubject("Feedback from " + feedback.getName());
        mailMessage.setText(feedback.getFeedback());

        //send email

        mailSender.send(mailMessage);
    }
}
