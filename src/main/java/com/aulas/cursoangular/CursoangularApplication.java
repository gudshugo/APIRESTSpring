package com.aulas.cursoangular;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@SpringBootApplication
public class CursoangularApplication implements CommandLineRunner{
	
	
	public static void main(String[] args)  {
		SpringApplication.run(CursoangularApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		
		
	}
	
	@Bean
	public FixedLocaleResolver getLocaleResolver(){
		return new FixedLocaleResolver(new Locale("pt", "BR"));
	}
}
