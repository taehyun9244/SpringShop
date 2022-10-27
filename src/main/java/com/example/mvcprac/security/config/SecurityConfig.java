package com.example.mvcprac.security.config;

import com.example.mvcprac.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccountService accountService;
    private final DataSource dataSource;

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        return new CustomAuthenticationProvider();
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean //TODO item 등록 막아놓기
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                    .authorizeRequests()
                    .antMatchers("/", "/login", "/signup", "/check-email-token", "/email-login", "/login-by-email", "/h2-console/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/items", "/profile/*", "/store", "/visa", "/school").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                .and()
                    .logout()
                .   logoutSuccessUrl("/")
                .and()
                    .rememberMe()
                    .userDetailsService(accountService)
                    .tokenRepository(tokenRepository())
                .and().build();
    }
}
