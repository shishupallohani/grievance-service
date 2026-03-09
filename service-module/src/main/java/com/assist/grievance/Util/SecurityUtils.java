package com.assist.grievance.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
	
	public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }

        return null;
    }
	
	public static String getAuthenticatedUserId() {
        // Keycloak/JWT removed, reuse principal name as fallback identifier.
        String username = getAuthenticatedUsername();
        if (username != null && !username.isBlank()) {
            return username;
        }
        return null;
    }
	

	private SecurityUtils() {
		throw new UnsupportedOperationException("This is a constant class and cannot be instantiated");
	}
	
	
	public static String getJwtTokenValue() {
	    // JWT auth is disabled.
	    return null;
	}


}
