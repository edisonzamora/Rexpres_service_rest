package com.security.jwt;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtTokenFilter extends OncePerRequestFilter {
	
	protected final  Logger logger = LogManager.getLogger(getClass());

	
	@Autowired
	private UserDetailsService userDetailsServicerexpres;

	@Autowired
	private JwtTokenOperator jwtTokenOperator;
	
	
	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
		logger.info("URI: "+request.getRequestURI());
        return request.getDispatcherType() != DispatcherType.REQUEST
                || request.getRequestURI().startsWith("/h2-console/**")
                || request.getRequestURI().startsWith("/v3/api-docs")
                || request.getRequestURI().startsWith("/swagger-ui.html")
                || request.getRequestURI().startsWith("/swagger-ui/index.html")
                || request.getRequestURI().startsWith("/.well-known/appspecific/com.chrome.devtools.json")
                || request.getRequestURI().startsWith("/auth/generarToken");
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info(">>doFilterInternal<<");
		try {
			// obtenemos la cabecera
			String header = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (header.isEmpty() || !header.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			// Obtenemos el Token
			String token = header.split(" ")[1].trim();
			token.replace("Bearer", "");
			if (!jwtTokenOperator.validate(token)) {
				filterChain.doFilter(request, response);
				return;
			}
			String nombre = jwtTokenOperator.getUsernameforToken(token);
			UserDetails userDetails = userDetailsServicerexpres.loadUserByUsername(nombre);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			e.getMessage();
		}
		filterChain.doFilter(request, response);
	}

}
