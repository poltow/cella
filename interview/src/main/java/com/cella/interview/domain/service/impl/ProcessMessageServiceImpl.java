package com.cella.interview.domain.service.impl;

import com.cella.interview.domain.model.Message;
import com.cella.interview.domain.service.ProcessMessageService;
import org.springframework.stereotype.Service;

@Service
public class ProcessMessageServiceImpl implements ProcessMessageService {

    @Override
    public Message processMessage(String message) {
        return new Message("ECHO: " + message);
    }
}
