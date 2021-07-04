package fr.cailliaud.springcloud;

import fr.cailliaud.springcloud.service.SimpleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Test consumer et producer")
class SpringCloudApplicationTest {

  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  InputDestination inputDestination;

  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  OutputDestination outputDestination;

  @Autowired SimpleProducer simpleProducer;

  @SpyBean SimpleService simpleService;

  @Test
  @DisplayName(
      "Quand un message est reçu sur le binding 'source', il est alors réémis vers le binding 'target'")
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
  @DisplayName("Un producer doit publier un évènement dans le binding 'target'")
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
  @DisplayName(
      "Quand un évènement est publié sur le binding 'somewhere', alors il doit être consommé")
  void doit_consommer_evenement() {
    // Given
    Message<String> message = MessageBuilder.withPayload("Coucou").build();
    // WHen
    inputDestination.send(message, "somewhere");
    // Then
    Mockito.verify(simpleService).log("Coucou");
  }

  @Test
  @DisplayName(
      "Un message consommé avec le header 'type'='upper' doit être rooté vers le service de log.")
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
