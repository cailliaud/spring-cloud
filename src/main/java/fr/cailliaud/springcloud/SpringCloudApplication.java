package fr.cailliaud.springcloud;

import fr.cailliaud.springcloud.service.SimpleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class SpringCloudApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudApplication.class);
  }

  @Bean
  public Function<Message<String>, Message<String>> forward() {
    return message -> message;
  }

  @Bean
  public Consumer<String> log(SimpleService simpleService) {
    return simpleService::log;
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
