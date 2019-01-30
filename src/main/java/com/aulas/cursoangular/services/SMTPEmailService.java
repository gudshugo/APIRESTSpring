package com.aulas.cursoangular.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SMTPEmailService extends AbstractEmailService{
	
	@Autowired
	private MailSender mailSender;
	
	private static final Logger log = LoggerFactory.getLogger(SMTPEmailService.class);
	
	@Override 
	public void sendEmail(SimpleMailMessage msg) {
		log.info("Enviando email...");
		mailSender.send(msg);
		log.info("Email enviado");
		
	}

}
 