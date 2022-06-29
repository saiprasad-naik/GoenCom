package com.goencom.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public boolean sendEmail(String subject, String message, String to) {
		boolean flag = false;
		String host = "smtp.gmail.com";
		String from = "vaibhavkalalofficial@gmail.com";
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("vaibhavkalalofficial@gmail.com", "opygawormrxfjont");
			}
		});

		session.setDebug(true);

		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			/* mimeMessage.setText(message); */
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
			flag = true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return flag;
	}

	public String otpMessageForEmailVerification(int otp) {
		return " <html lang=\"en\">\n" + "<head>\n" + "    <meta charset=\"UTF-8\">\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "        <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n"
				+ "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n"
				+ "    <link href=\"https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;1,200&display=swap\"\n"
				+ "        rel=\"stylesheet\">\n" + "    <title>OTP Email</title>\n" + "    <style>\n" + "    * {\n"
				+ "        margin: 0;\n" + "        padding: 0;\n" + "        box-sizing: border-box;\n"
				+ "        font-family: 'Poppins',\n" + "            sans-serif;\n" + "    }\n" + "\n" + "    .won{\n"
				+ "        width: 70%;\n" + "        margin: 8rem auto;\n"
				+ "        border-bottom: 5px solid rgb(98, 98, 223);\n" + "    }\n" + "\n" + "    h1{\n"
				+ "        text-align: center;\n" + "        font-size: 2.5rem;\n" + "        font-weight: 900;\n"
				+ "        margin: 2rem auto;\n" + "        color: rgb(98, 98, 223);\n" + "    }\n" + "\n" + "    h4{\n"
				+ "        text-align: center;\n" + "        margin: 1rem auto;\n" + "        font-size: 1.5rem;\n"
				+ "        font-weight: 600;\n" + "    }\n" + "\n" + "    p{\n" + "        margin: 2.5rem auto;\n"
				+ "        text-align: center;\n" + "    }\n" + "\n" + "    span{\n" + "        font-weight: 600;\n"
				+ "    }\n" + "\n" + "\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    \n"
				+ "    <div class=\"won\">\n" + "\n" + "        <h1>GoenCom.</h1>\n" + "\n"
				+ "        <h4>OTP for reset Password!</h4>\n" + "\n"
				+ "        <p>Dear user, this email has been sent to you to with the OTP to be used to reset your user password at GoenCom.!</p>\n"
				+ "        <p>The OTP is: <span id=\"otp\">" + otp + "</span> !</p>\n" + "\n" + "    </div>\n" + "\n"
				+ "</body>\n" + "</html>";
	}

	public String otpMessage(int otp) {
		return " <html lang=\"en\">\n" + "<head>\n" + "    <meta charset=\"UTF-8\">\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "        <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n"
				+ "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n"
				+ "    <link href=\"https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;1,200&display=swap\"\n"
				+ "        rel=\"stylesheet\">\n" + "    <title>OTP Email</title>\n" + "    <style>\n" + "    * {\n"
				+ "        margin: 0;\n" + "        padding: 0;\n" + "        box-sizing: border-box;\n"
				+ "        font-family: 'Poppins',\n" + "            sans-serif;\n" + "    }\n" + "\n" + "    .won{\n"
				+ "        width: 70%;\n" + "        margin: 8rem auto;\n"
				+ "        border-bottom: 5px solid rgb(98, 98, 223);\n" + "    }\n" + "\n" + "    h1{\n"
				+ "        text-align: center;\n" + "        font-size: 2.5rem;\n" + "        font-weight: 900;\n"
				+ "        margin: 2rem auto;\n" + "        color: rgb(98, 98, 223);\n" + "    }\n" + "\n" + "    h4{\n"
				+ "        text-align: center;\n" + "        margin: 1rem auto;\n" + "        font-size: 1.5rem;\n"
				+ "        font-weight: 600;\n" + "    }\n" + "\n" + "    p{\n" + "        margin: 2.5rem auto;\n"
				+ "        text-align: center;\n" + "    }\n" + "\n" + "    span{\n" + "        font-weight: 600;\n"
				+ "    }\n" + "\n" + "\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    \n"
				+ "    <div class=\"won\">\n" + "\n" + "        <h1>GoenCom.</h1>\n" + "\n"
				+ "        <h4>OTP for reset Password!</h4>\n" + "\n"
				+ "        <p>Dear user, this email has been sent to you to with the OTP to be used to verify you email at GoenCom.!</p>\n"
				+ "        <p>The OTP is: <span id=\"otp\">" + otp + "</span> !</p>\n" + "\n" + "    </div>\n" + "\n"
				+ "</body>\n" + "</html>";
	}

	public String bidWonMessage() {
		return "<head>\n" + "    <meta charset=\"UTF-8\">\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "        <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n"
				+ "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n"
				+ "    <link href=\"https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;1,200&display=swap\"\n"
				+ "        rel=\"stylesheet\">\n" + "    <title>Won Email</title>\n" + "    <style>\n" + "    * {\n"
				+ "        margin: 0;\n" + "        padding: 0;\n" + "        box-sizing: border-box;\n"
				+ "        font-family: 'Poppins',\n" + "            sans-serif;\n" + "    }\n" + "\n" + "    .won{\n"
				+ "        width: 70%;\n" + "        margin: 8rem auto;\n"
				+ "        border-bottom: 5px solid rgb(98, 98, 223);\n" + "    }\n" + "\n" + "    h1{\n"
				+ "        text-align: center;\n" + "        font-size: 2.5rem;\n" + "        font-weight: 900;\n"
				+ "        margin: 2rem auto;\n" + "        color: rgb(98, 98, 223);\n" + "    }\n" + "\n" + "    h4{\n"
				+ "        text-align: center;\n" + "        margin: 1rem auto;\n" + "        font-size: 1.5rem;\n"
				+ "        font-weight: 600;\n" + "    }\n" + "\n" + "    p{\n" + "        margin: 2.5rem auto;\n"
				+ "        text-align: center;\n" + "    }\n" + "\n" + "\n" + "\n" + "    </style>\n" + "</head>\n"
				+ "<body>\n" + "    \n" + "    <div class=\"won\">\n" + "\n" + "        <h1>GoenCom.</h1>\n" + "\n"
				+ "        <h4>Congratulations for winning the bid!!</h4>\n" + "\n"
				+ "        <p>Dear user, this email has been sent to you to notify that you have successfully won the bid you had placed on an item!</p>\n"
				+ "        <p>Kindly check out your bidding history on the GoenCom. website for more information.</p>\n"
				+ "\n" + "    </div>\n" + "\n" + "</body>";
	}
}
