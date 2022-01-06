package dev.rmjr.todo.config;

import dev.rmjr.todo.interceptor.TokenAuthenticationFilter;
import dev.rmjr.todo.repository.UserRepository;
import dev.rmjr.todo.service.CustomUserDetailsService;
import dev.rmjr.todo.service.KeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final KeyService keyService;
    private final EncoderConfig encoderConfig;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/users/confirm").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/users").authenticated()
                    .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(userDetailsService(), keyService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication
                .authenticationProvider(authenticationProvider())
                .userDetailsService(userDetailsService());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(encoderConfig.encoder());
        return authenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }
}
