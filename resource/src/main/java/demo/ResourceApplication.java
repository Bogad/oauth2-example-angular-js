package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ma.co.omnidata.framework.services.businessInterface.IFacade;
import ma.co.omnidata.framework.services.businessInterface.IMessageItem;
import ma.co.omnidata.framework.services.businessInterface.IResult;
import ma.co.omnidata.framework.services.businessInterface.impl.ErrorMessageItem;
import ma.co.omnidata.framework.services.core.IServices;
import ma.co.omnidata.framework.services.core.ServicesFactory;
import ma.co.omnidata.framework.services.securite.IActionSecurise;
import ma.co.omnidata.framework.services.securite.impl.GenericActionImpl;
import omnishore.omnirh.server.admin.metier.valueobjects.ActionVO;
import omnishore.omnirh.server.utile.IActionSecurisees;

@SpringBootApplication
@RestController
@EnableResourceServer
@ImportResource("classpath:/context.xml")
public class ResourceApplication {

	@Autowired
	IFacade omniFacade;
	@Autowired
	CustomRemoteServices tokenService;
	@Autowired
	IServices services;

	@RequestMapping("/userHasRole")
	public RoleCheck userHasRole() {
		//TODO check the real informations for the connected user
		RoleCheck check = new RoleCheck("KO");
		check.setRoles(new ArrayList<String>() {{
		    add("Administrateur");
		    add("Chargé de comptes");
		    add("Responsable Archive");
		}});
		
		return check;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/selectRole")
	public String roles(@RequestBody String  role) {
		
		System.out.println("####### Rôle = "+role);
		System.out.println("####### token = "+tokenService.getAccessToken());
		return "OK";
	}

	@RequestMapping("/")
	public Message home() {
		return new Message("Hello World");
	}

	@RequestMapping("/call")
	public String testCall() {
		return "Hello from Call";
	}

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@Configuration
	class ResourceServerConfig extends ResourceServerConfigurerAdapter {

		private static final String RESOURCE_ID = "person";

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated().and().csrf().disable();
		}

		@Primary
		@Bean
		public CustomRemoteServices tokenService() {
			CustomRemoteServices tokenService = new CustomRemoteServices();
			tokenService.setCheckTokenEndpointUrl("http://localhost:9999/uaa/oauth/check_token");
			tokenService.setClientId("omnirh");
			tokenService.setClientSecret("omnirh");
			tokenService.setAccessTokenConverter(accessTokenConverter());
			return tokenService;
		}

		@Bean
		public AccessTokenConverter accessTokenConverter() {
			return new DefaultAccessTokenConverter();
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId("omnirh-server");
		}

	}
}

class Message {
	private String id = UUID.randomUUID().toString();
	private String content;

	Message() {
	}

	public Message(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}

class RoleCheck {
	private String check;
	private List<String> roles;

	RoleCheck() {
	}

	public RoleCheck(String check) {
		this.check = check;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
