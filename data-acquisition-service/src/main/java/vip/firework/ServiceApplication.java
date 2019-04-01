package vip.firework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class ServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServiceApplication.class, args);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
