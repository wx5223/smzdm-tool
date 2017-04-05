package com.shawn.microservice.config;

import com.shawn.microservice.config.beans.WebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Created by LY on 2015/11/18.
 */
public class AppBaseConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public WebConfig webConfig(){
        return new WebConfig();
    }

    /*@Bean
    public EurekaInstanceConfig eurekaInstanceConfig() {
        return new EurekaInstanceConfig();
    }*/
}
