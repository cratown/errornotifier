package pl.dreamcode.errornotifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class TestWebSecurityConfig extends WebSecurityConfig {
    
	@Override
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build();
        // UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("USER", "ADMIN").build();
        // UserDetails superadmin = User.withUsername("superadmin").password(passwordEncoder().encode("superadmin")).roles("USER", "ADMIN", "SUPERADMIN").build();
		return new InMemoryUserDetailsManager(user);
	}
}
