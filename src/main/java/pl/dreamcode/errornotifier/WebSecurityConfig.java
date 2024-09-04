package pl.dreamcode.errornotifier;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import pl.dreamcode.errornotifier.users.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, @Value("${http.rememberMeKey}") final String rememberMeKey) throws Exception {
		http
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/errors.json", "/api/**"))
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/errors/**", "/admin/**").authenticated()
				.anyRequest().permitAll()
			)
			.formLogin((form) -> form
				.loginPage("/login")
			)
			.rememberMe(rememberMe -> rememberMe.key(rememberMeKey)
				.tokenValiditySeconds((int)Duration.ofDays(180).getSeconds())
				.rememberMeParameter("rememberMe"))
			.logout((logout) -> logout.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
