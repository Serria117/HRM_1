package com.hrm.services;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.emailDto.EmailDetails;
import com.hrm.services.contract.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

@Service @Slf4j @RequiredArgsConstructor
public class EmailServiceImpl implements EmailService
{

    private final JavaMailSender javaMailSender;

    String senderName = "Administrator";

    @Override @Async
    public CompletableFuture<BaseResponse> sendSimpleEmail(EmailDetails emailDetails)
    {
        try {
            var mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderName);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            return CompletableFuture.completedFuture(BaseResponse.success("Email sent"));
        }
        catch ( Exception e ) {
            return CompletableFuture.completedFuture(BaseResponse.error(e.getMessage()));
        }
    }

    @Override @Async
    public CompletableFuture<BaseResponse> sendFormattedEmail(EmailDetails emailDetails)
    {

        var message = javaMailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderName);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getMsgBody(), true);

            javaMailSender.send(message);
            return CompletableFuture.completedFuture(BaseResponse.success("Email sent"));
        }
        catch ( MessagingException e ) {
            return CompletableFuture.completedFuture(BaseResponse.error(e.getMessage()));
        }
    }
}
