package edu.sabanciuniv.hotelbookingapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RedirectUtil {

    public static String getRedirectUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                return "/admin/dashboard";
            } else if (grantedAuthority.getAuthority().equals("ROLE_CUSTOMER")) {
                return "/search";
            } else if (grantedAuthority.getAuthority().equals("ROLE_HOTEL_MANAGER")) {
                return "/manager/dashboard";
            }
        }
        return null;
    }

}
