package com.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import com.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.security.*")
public class RexpresSecurityConfig extends WebSecurityConfigurerAdapter {

	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier("passwordEncoder")
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	@Qualifier("jwtTokenRexpresFilter")
	public JwtTokenFilter jwtTokenFilter;

	private static final RequestMatcher EXCLUDE_SECURITY;
	static {

		List<RequestMatcher> list = new ArrayList<RequestMatcher>();
		list.add(new AntPathRequestMatcher("/h2-console/**"));
		list.add(new AntPathRequestMatcher("/swagger-ui.html"));
		list.add(new AntPathRequestMatcher("/swagger-ui/**"));
		list.add(new AntPathRequestMatcher("/v3/**"));
		list.add(new AntPathRequestMatcher("/error"));
		list.add(new AntPathRequestMatcher("/favicon.ico"));
		list.add(new AntPathRequestMatcher("/.well-known/appspecific/com.chrome.devtools.json"));
	
		EXCLUDE_SECURITY = new OrRequestMatcher(list);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	
	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers("/h2-console/**")
		              .antMatchers("/v3/**")
		              .antMatchers("/swagger-ui.html")
		              .antMatchers("/swagger-ui/index.html")
		              .antMatchers("/swagger-ui/index.html")
		              .antMatchers("/.well-known/appspecific/com.chrome.devtools.json")
		              .antMatchers("/auth/generarToken")
		              ;
		super.configure(web);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncode);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.info(">>configure security rexpresRestService<<");
		/*
		 * anulamos el estado de la sesión
		 */
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		/* 
		 * Habilitar CORS y deshabilitar CSRF
		 * */
		http.headers().frameOptions().sameOrigin();
		http.cors().and().csrf().disable()
				/*
				 * Establecemos los perimos a las rutas
				 */
				.authorizeRequests().requestMatchers(EXCLUDE_SECURITY).permitAll()
				.antMatchers("/auth/alta").hasRole("ADMIN")
				.anyRequest().authenticated();

		// controlador de excepciones solicitudes no autorizadas
		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				logger.error(">>>>>>>>>>>>>>>>>>>> Fallo authenticationEntryPoint: " + authException.getMessage());
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(401);
				response.getWriter().write("{\"status\":401,\"timestamp\":" + System.currentTimeMillis()
						+ ",\"mensage\":\"No autentificado\"}");

			}
		});
		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				logger.error(">>>>>>>>>>>>>>>>>>>> Fallo accessDeniedHandler: " + accessDeniedException.getMessage());
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(403);
				response.getWriter().write("{\"status\":403,\"timestamp\":" + System.currentTimeMillis()
						+ ",\"mensage\":\"Acceso denegado, compruebe sus roles\"}");

			}
		});

		// añadir el fitro que valida el token
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
