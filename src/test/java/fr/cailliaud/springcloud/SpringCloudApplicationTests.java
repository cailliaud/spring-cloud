package fr.cailliaud.springcloud;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestChannelBinderConfiguration.class)
class SpringCloudApplicationTests {

  @Autowired InputDestination inputDestination;
  @Autowired OutputDestination outputDestination;

  @Test
  void doit_reemettre_message() {
    // Given
    Message<String> message =
        MessageBuilder.withPayload("Coucou").setHeader("type", "MESSAGE_IMPORT").build();

    // When
    inputDestination.send(message, "source");

    // THen
    Message<byte[]> messageReceived = outputDestination.receive(0, "target");
    Assertions.assertThat(new String(messageReceived.getPayload(), StandardCharsets.UTF_8))
        .isEqualTo(message.getPayload());
  }
}
