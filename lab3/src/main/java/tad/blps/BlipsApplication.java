package tad.blps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan("tad.blps.entity")
@EnableTransactionManagement
public class BlipsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlipsApplication.class, args);
    }

}