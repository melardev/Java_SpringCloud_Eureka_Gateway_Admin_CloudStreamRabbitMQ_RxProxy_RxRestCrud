package com.melardev.cloud.rest.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TodoDataStreamSource {
    @Output("todo-events-output-channel")
    MessageChannel getMessageChannel();
}
