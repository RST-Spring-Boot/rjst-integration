package de.rjst.rjstintegration.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class RestIntegrationConfig {

    @Bean
    public MessageChannel insertChannel() {
        return new DirectChannel();
    }


    @Bean
    public MessageChannel replyChannel() {
        return new DirectChannel();
    }

}
