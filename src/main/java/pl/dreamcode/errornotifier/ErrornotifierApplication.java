package pl.dreamcode.errornotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAsync
public class ErrornotifierApplication implements WebMvcConfigurer  {

	public static void main(String[] args) {
		SpringApplication.run(ErrornotifierApplication.class, args);
	}
}
