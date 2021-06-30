package fr.cailliaud.springcloud;

import fr.cailliaud.springcloud.service.SimpleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestChannelBinderConfiguration.class)
class SpringCloudApplicationTest {

  @Autowired InputDestination inputDestination;
  @Autowired OutputDestination outputDestination;
  @Autowired SimpleProducer simpleProducer;

  @SpyBean SimpleService simpleService;

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

  @Test
  void doit_reemettre_message_provenant_producer() {
    // Given
    String payload = "Coucou";
    String headerType = "MESSAGE_IMPORT";

    // When
    simpleProducer.produceEvent(payload, headerType);

    // THen
    Message<byte[]> messageReceived = outputDestination.receive(0, "target");
    Assertions.assertThat(new String(messageReceived.getPayload(), StandardCharsets.UTF_8))
        .isEqualTo(payload);
  }

  @Test
  void doit_consommer_evenement() {
    // Given
    Message<String> message = MessageBuilder.withPayload("Coucou").build();
    // WHen
    inputDestination.send(message, "somewhere");
    // Then
    Mockito.verify(simpleService).log("Coucou");
  }

  @Test
  void test() {
    // Given
    Message<String> message =
        MessageBuilder.withPayload("Coucou").setHeader("type", "upper").build();
    // WHen
    inputDestination.send(message, "nowhere");
    // Then
    Mockito.verify(simpleService).log("COUCOU");
  }
}
