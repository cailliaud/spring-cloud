package fr.cailliaud.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
public class SimpleProducer {

  @Autowired private StreamBridge streamBridge;

  public void produceEvent(String payload, String headerType) {
    Message<String> message =
        MessageBuilder.withPayload(payload).setHeader("type", headerType).build();
    streamBridge.send("forward-in-0", message, MimeTypeUtils.APPLICATION_JSON);
  }
}
