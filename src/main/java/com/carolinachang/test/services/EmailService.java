package com.carolinachang.test.services;

import org.springframework.mail.SimpleMailMessage;

import com.carolinachang.test.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
