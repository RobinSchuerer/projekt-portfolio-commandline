package de.lv1871.projektportfolio.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by SchuererR on 02.08.2016.
 */
@SpringBootApplication
@ComponentScan(basePackages = "de.lv1871.projektportfolio")
public class ProjektPortfolioApplication {


    public static void main(String[] args) {

       SpringApplication.run(ProjektPortfolioApplication.class, args);


    }

}

