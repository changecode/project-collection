package cn.sunline.spring.cloud.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
@EnableAutoConfiguration
@EnableFeignClients
@EnableDiscoveryClient
public class Application {

    public static void main(String[] args) {
        // new SpringApplicationBuilder(Application.class).web(true).run(args);
    	SpringApplication.run(Application.class, args);
    }
}
