package com.codingdojo.springjwt;

import java.util.ArrayList;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.codingdojo.springjwt.models.Category;
import com.codingdojo.springjwt.models.Role;
import com.codingdojo.springjwt.models.Store;
import com.codingdojo.springjwt.models.User;
import com.codingdojo.springjwt.services.CategoryService;
import com.codingdojo.springjwt.services.StoreService;
import com.codingdojo.springjwt.services.UserService;

@SpringBootApplication
public class SpringJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtApplication.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setPort(9090);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setScheme("http");
((AbstractAjpProtocol)ajpConnector.getProtocolHandler()).setSecretRequired(false);
        tomcat.addAdditionalTomcatConnectors(ajpConnector);
        return tomcat;
    }
	
	
	@Bean
	CommandLineRunner run(UserService userService, StoreService storeService, CategoryService categoryService) {
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_MANAGER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
			
			User userAux = new User(null,"Renato", "Garay","renato@gmail.com","1234",new ArrayList<>(),null,null);
			
			userService.saveUser(userAux);
			userService.saveUser(new User(null,"Vanessa","Lopez","vanessa@gmail.com","1234",new ArrayList<>(),null,null));
			userService.saveUser(new User(null,"Victoria", "Miranda","victoria@gmail.com","1234",new ArrayList<>(),null,null));
			userService.saveUser(new User(null,"Sandra" ,"Delgado","sandra@gmail.com","1234",new ArrayList<>(),null,null));
			
			userService.addRoleToUser("renato@gmail.com", "ROLE_USER");
			userService.addRoleToUser("renato@gmail.com", "ROLE_MANAGER");
			userService.addRoleToUser("vanessa@gmail.com", "ROLE_MANAGER");
			userService.addRoleToUser("victoria@gmail.com", "ROLE_ADMIN");
			userService.addRoleToUser("sandra@gmail.com", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("sandra@gmail.com", "ROLE_ADMIN");
			userService.addRoleToUser("sandra@gmail.com", "ROLE_USER");
			
			Store newStore = storeService.saveStore(new Store(null,"prueba",userAux,null,null,null,null,null,null));
			
			Category newCategory = new Category(null,"Categoria 1",1,newStore,null);
			categoryService.saveCategory(newCategory);
			
		};
	}

}
