package com.proofchain.shared.util;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public void sendEmail(String destinatario, String codigo) {
        try {
            MimeMessage mensagem = mailSender.createMimeMessage();
            // O parâmetro "true" habilita multipart (necessário para HTML + imagens embutidas)
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject("ProofChain - Código de Validação de Senha");

            String corpoHtml = """
                <div style="
                  font-family: Arial, sans-serif;\s
                  max-width: 500px;\s
                  margin: auto;\s
                  padding: 25px;\s
                  border: 1px solid #acabab;\s
                  border-radius: 10px;\s
                  background-color: #dadada;">
    
                  <!-- Logo Centralizado -->
                  <div style="text-align: center; margin-bottom: 20px;">
                      <img src="cid:logoProofchain" alt="ProofChain" style="max-width: 180px; height: auto;" />
                  </div>
    
                  <h2 style="color: #222222; text-align: center; font-size: 20px;">Validação de Alteração de Senha</h2>
    
                  <!-- Texto Explicativo -->
                  <p style="color: #555555; font-size: 15px; line-height: 1.5; text-align: center;">
                      Olá! Recebemos uma solicitação para alterar a senha da sua conta na <strong>ProofChain</strong>.\s
                      Para confirmar que foi você e concluir a alteração, insira o código de verificação abaixo na plataforma:
                  </p>
    
                  <!-- Token Centralizado e em Negrito -->
                  <div style="background-color: #f4f6f8; padding: 18px; margin: 25px 0; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #1a202c; border-radius: 8px; border: 1px dashed #cbd5e0;">
                      %s
                  </div>
    
                  <p style="color: #888888; font-size: 12px; text-align: center; margin-top: 20px;">
                      Este código é válido por <strong>10 minutos</strong>.<br>
                      Caso você não tenha solicitado esta alteração, ignore este e-mail com segurança.
                  </p>
              </div>
                """.formatted(codigo);

            // Define o HTML (deve ser chamado ANTES do addInline)
            helper.setText(corpoHtml, true);

            // Anexa a imagem que está em src/main/resources/static/images/logo-proofchain.png
            ClassPathResource logo = new ClassPathResource("static/images/logo_horizontal_light.png");
            helper.addInline("logoProofchain", logo);

            mailSender.send(mensagem);

        } catch (MessagingException e) {
            throw new MailSendException("Erro ao enviar o e-mail de validação da ProofChain", e) {
            };
        }
    }
}
