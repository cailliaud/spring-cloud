package fr.cailliaud.springcloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleService.class);

  public void log(String payload) {
    LOGGER.info("We just received a message with the payload '{}'", payload);
  }
}
