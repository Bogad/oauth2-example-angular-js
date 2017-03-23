package demo;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
public class UiApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.logout().and().authorizeRequests().antMatchers("/index.html", "/home.html", "/login").permitAll()
				.anyRequest().authenticated().and().csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		// @formatter:on
	}

//	@Configuration
//	class configServlet extends SpringBootServletInitializer {
//		@Bean
//		public ServletRegistrationBean dispatcherServletRegistration() {
//
//		    ServletRegistrationBean registration = new ServletRegistrationBean(
//		            dispatcherServlet(), "/roles/*");
//
//		    registration
//		            .setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
//
//		    return registration;
//		}
//		@Bean
//		public Servlet dispatcherServlet() {
//			return new HttpServlet() {
//				@Override
//				protected void doGet(HttpServletRequest req, HttpServletResponse res)
//						throws ServletException, IOException {
//					  final String uri = "http://localhost:9000/resource/roles";
//					     
//					    RestTemplate restTemplate = new RestTemplate();
//					    String result = restTemplate.getForObject(uri, String.class);
//					     
//					    System.out.println(result);
//				}
//			};
//		}
//
//	}
}
