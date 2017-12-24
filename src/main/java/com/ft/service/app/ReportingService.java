package com.ft.service.app;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ReportingService {

	@Autowired
	SimpMessageSendingOperations messagingTemplate;

//	@Scheduled(fixedRate = 10000)
	public void reportActivity() {
		messagingTemplate.convertAndSend("/topic/logging", ZonedDateTime.now() + " -- Activity Sent");
	}
}
