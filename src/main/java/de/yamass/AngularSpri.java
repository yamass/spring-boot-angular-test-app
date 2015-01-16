package de.yamass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Yann Massard
 */
@EnableAutoConfiguration
@ComponentScan
public class AngularSpri {

    public static void main(String[] args) {
        SpringApplication.run(AngularSpri.class, args);
    }


}
