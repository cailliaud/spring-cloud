package fr.cailliaud.springcloud;

import fr.cailliaud.springcloud.service.SimpleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SimpleConsumerConfiguration {

  @Bean
  public Consumer<String> log(SimpleService simpleService) {
    return message -> simpleService.log(message);
  }

  @Bean
  public Consumer<String> upper(SimpleService simpleService) {
    return message -> simpleService.log(message.toUpperCase());
  }

  @Bean
  public Consumer<String> lower(SimpleService simpleService) {
    return message -> simpleService.log(message.toLowerCase());
  }
}
