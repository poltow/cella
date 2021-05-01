package com.cella.interview.app.rest;

import com.cella.interview.domain.model.Message;
import com.cella.interview.domain.service.ProcessMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cella.interview.app.api.MessageApi;

@RestController
@RequestMapping(path = "/message")
public class MessageController {

	@Autowired
	private ProcessMessageService messageService;

	@PostMapping("/echo")
	public ResponseEntity<?> echo(@RequestBody MessageApi message) {
		return new ResponseEntity<Message>(messageService.processMessage(message.getMessage()), HttpStatus.OK);
	}
}
