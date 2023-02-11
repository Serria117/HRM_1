package com.hrm.services.contract;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.emailDto.EmailDetails;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface EmailService
{
    @Async
    CompletableFuture<BaseResponse> sendSimpleEmail(EmailDetails emailDetails);
    @Async
    CompletableFuture<BaseResponse> sendFormattedEmail(EmailDetails emailDetails);
}
