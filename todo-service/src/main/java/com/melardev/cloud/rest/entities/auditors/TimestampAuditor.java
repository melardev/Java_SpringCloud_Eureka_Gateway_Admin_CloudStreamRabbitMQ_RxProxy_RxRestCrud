package com.melardev.cloud.rest.entities.auditors;


import com.melardev.cloud.rest.entities.TimeStampedDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampAuditor extends AbstractMongoEventListener<TimeStampedDocument> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<TimeStampedDocument> event) {
        if (event.getSource().getCreatedAt() == null)
            event.getSource().setCreatedAt(LocalDateTime.now());
        event.getSource().setUpdatedAt(LocalDateTime.now());
    }
}
