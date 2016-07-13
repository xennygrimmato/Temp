package com.devfactory.assignment4;

/**
 * Created by vaibhavtulsyan on 07/07/16.
 */

import com.devfactory.assignment4.queue.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestInterceptor requestInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}