package be.codecoach.security;

import be.codecoach.security.authentication.OnAuthenticationFailureHandler;
import be.codecoach.security.authentication.jwt.JwtAuthenticationFilter;
import be.codecoach.security.authentication.jwt.JwtAuthorizationFilter;
import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.SecuredUserService;
import be.codecoach.security.authentication.user.api.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Profile("production")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final SecuredUserService securedUserService;
    private final AccountService accountService;

    @Autowired
    public SecurityConfig(SecuredUserService securedUserService,
                          PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator,
                          AccountService accountService) {

        this.securedUserService = securedUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.accountService = accountService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .and().authorizeRequests().antMatchers("/swagger**").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.GET, "/**").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.PUT, "/users/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), authenticationFailureHandler(), jwtGenerator, accountService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), authenticationFailureHandler(), jwtGenerator))
                //.formLogin().permitAll() // Add .loginPage("/login") in the middle once it has been made
                //.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new OnAuthenticationFailureHandler();
    }

    @Bean
    CorsFilter corsFilter(@Value("${youcoach.allowed.origins}") String origins) {

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setMaxAge(8000L);
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin(origins);
        corsConfig.addAllowedOrigin("https://rt-codecoach.netlify.app/");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("PATCH");
        corsConfig.addAllowedMethod("DELETE");


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securedUserService).passwordEncoder(passwordEncoder);
    }
}
