package br.com.fiap.mgmtmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HackaMscMgmtMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackaMscMgmtMediaApplication.class, args);
    }

}
