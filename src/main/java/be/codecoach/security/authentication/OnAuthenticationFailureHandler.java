package be.codecoach.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OnAuthenticationFailureHandler implements AuthenticationFailureHandler {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        switch (e.getClass().getSimpleName()) {
            case "DisabledException":
                sendErrorResponse(httpServletResponse, HttpStatus.UNAUTHORIZED, "USER_DISABLED");
                break;
            case "BadCredentialsException":
            case "AuthenticationCredentialsNotFoundException":
                sendErrorResponse(httpServletResponse, HttpStatus.UNAUTHORIZED, "USER_UNKNOWN");
                break;
            default:
                System.out.println("ERROR: " + e.getClass().getSimpleName());
                sendErrorResponse(httpServletResponse, HttpStatus.FORBIDDEN, e.getClass().getSimpleName());
        }

    }

    private void sendErrorResponse(HttpServletResponse httpServletResponse, HttpStatus status, String error) throws IOException {
        httpServletResponse.setStatus(status.value());
        httpServletResponse.getOutputStream()
                .println(objectMapper.writeValueAsString(new OnAuthenticationFailureActionResponse(error)));
    }

    public static class OnAuthenticationFailureActionResponse {
        private String name;

        public OnAuthenticationFailureActionResponse(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
