package com.example.ProductCategory.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Get all roles of logged-in user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // 🔥 Role-based redirection
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/getAllProducts/0");
        }
        else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/product/getHomePage/0");

        } else if (roles.contains("ROLE_SELLER")) {
            response.sendRedirect("/product/showMyProduct/0");

        } else {
            response.sendRedirect("/public/Login");
        }
    }
}
