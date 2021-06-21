package com.alchemistry.utils.config;

import com.alchemistry.security.jwt.JwtConfigurer;
import com.alchemistry.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.alchemistry.utils.AlchemistryContants.LOGIN_ALCHEMIST_ENDPOINT;
import static com.alchemistry.utils.AlchemistryContants.MAGISTER_ALCHEMIST;
import static com.alchemistry.utils.AlchemistryContants.MAGISTER_ALCHEMIST_ENDPOINT;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ALCHEMIST_ENDPOINT).permitAll()
                .antMatchers(MAGISTER_ALCHEMIST_ENDPOINT).hasRole(MAGISTER_ALCHEMIST)
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
