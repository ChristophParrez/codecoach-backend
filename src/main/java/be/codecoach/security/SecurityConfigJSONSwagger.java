package be.codecoach.security;

import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.SecuredUserService;
import be.codecoach.security.authentication.user.api.AccountService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("development")
@EnableWebSecurity
public class SecurityConfigJSONSwagger extends SecurityConfig {

        private final SecuredUserService securedUserService;
        private final PasswordEncoder passwordEncoder;

        public SecurityConfigJSONSwagger(SecuredUserService securedUserService,
                                         PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, AccountService accountService) {
            super(securedUserService, passwordEncoder, jwtGenerator, accountService);

            this.securedUserService = securedUserService;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers("/v3/api-docs/**",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/configuration/security",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**");
            super.configure(web);
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(securedUserService).passwordEncoder(passwordEncoder);
            super.configure(auth);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/v3/api-docs/**",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/configuration/security",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**").permitAll();

            super.configure(http);

        }
}
