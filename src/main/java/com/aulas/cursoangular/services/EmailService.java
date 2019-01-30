package com.aulas.cursoangular.services;

import org.springframework.mail.SimpleMailMessage;

import com.aulas.cursoangular.domain.Cliente;
import com.aulas.cursoangular.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
	
}
