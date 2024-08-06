package io.madhu.toyataSalesAnalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class KStreamToyotaSalesAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KStreamToyotaSalesAnalyticsApplication.class, args);
    }
}
