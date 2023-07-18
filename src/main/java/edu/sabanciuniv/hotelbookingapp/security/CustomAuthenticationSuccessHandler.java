package edu.sabanciuniv.hotelbookingapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.warn("Redirecting an authenticated user to the appropriate dashboard");

        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_CUSTOMER")) {
                redirectUrl = "/customer/dashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_HOTEL_MANAGER")) {
                redirectUrl = "/manager/dashboard";
                break;
            }
        }
        log.info("Redirect path: " + redirectUrl);

        if (redirectUrl == null) {
            throw new IllegalStateException("Could not determine user role for redirection");
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
