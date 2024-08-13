package pl.dreamcode.errornotifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class ErrornotifierApplicationTests {

	@MockBean
    private JavaMailSender mailSender;

	@Autowired
    private Environment env;

	@Test
	void contextLoads() {
		assertEquals("jdbc:h2:mem:public", env.getProperty("spring.datasource.url"));
	}

}
