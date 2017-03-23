package com.example;

import java.security.Principal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.example.custom.service.UserDetailsServiceImpl;

@SpringBootApplication
@Controller
@SessionAttributes("authorizationRequest")
@EnableResourceServer
@EnableAuthorizationServer
@EnableJpaRepositories
public class AuthorizationApplication {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}

	@Configuration
	static class MvcConfig extends WebMvcConfigurerAdapter {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("login").setViewName("login");
			registry.addViewController("/").setViewName("index");
		}
	}

	@Configuration
	@Order(-20)
	static class LoginConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private UserDetailsServiceImpl userDetailsService;

		@Bean
		public HibernateJpaSessionFactoryBean sessionFactory() {
			return new HibernateJpaSessionFactoryBean();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.formLogin().loginPage("/login").permitAll().and().requestMatchers()
					.antMatchers("/", "/login", "/oauth/authorize", "/oauth/confirm_access").and().authorizeRequests()
					.anyRequest().authenticated();
//			http.addFilterAfter(new BuildAuthorityFilterBean(), BasicAuthenticationFilter.class);
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new Md5PasswordEncoder();
		}

		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
			authenticationProvider.setUserDetailsService(userDetailsService);
			authenticationProvider.setPasswordEncoder(passwordEncoder());
			return authenticationProvider;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
			// authManagerBuilder.inMemoryAuthentication().withUser("user1").password("password1").roles("USER").and()
			// .withUser("admin1").password("password1").roles("ADMIN");
			authManagerBuilder.userDetailsService(userDetailsService);
			authManagerBuilder.authenticationProvider(authenticationProvider());
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
		}
	}

	@Configuration
	public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
//			clients.inMemory().withClient("omnirh").secret("omnirh").authorizedGrantTypes("password")
//					.resourceIds("omnirh-server").scopes("read", "write").and().withClient("acme").secret("acmesecret").redirectUris("http://localhost:8080/login","http://localhost:8080/home.html")
//					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//					.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write", "openid")
//					.autoApprove(true);
			 clients.jdbc(dataSource);
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager).accessTokenConverter(defaultAccessTokenConverter());
		}

		@Bean
		public DefaultAccessTokenConverter defaultAccessTokenConverter() {
			return new DefaultAccessTokenConverter();
		}

		@Override
		public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
			security.checkTokenAccess("authenticated"); // enable
														// '/oauth/check_token'
														// endpoint
		}
	}
}
