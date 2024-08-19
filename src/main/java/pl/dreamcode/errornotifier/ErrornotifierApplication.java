package pl.dreamcode.errornotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(info = @Info(title="Error notifier API"))
public class ErrornotifierApplication implements WebMvcConfigurer  {

	public static void main(String[] args) {
		SpringApplication.run(ErrornotifierApplication.class, args);
	}
}
