/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
*/

package nl.waisda.services;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nl.waisda.domain.ResetPassword;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private Logger log = Logger.getLogger(MailService.class);

	@Autowired
	private JavaMailSender mailSender;

	public void sendPasswordResetMail(final ResetPassword reset) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

				// TODO Prepend hostname here:
				String url = String.format("/wachtwoord-veranderen?id=%d&key=%s",
						reset.getId(), reset.getPlainTextKey());

				String body = "Beste " + reset.getUser().getEmail() + ",\n\n";
				body += "Een verzoek is ingediend om uw wachtwoord te vervangen op Waisda?.\n\n";
				body += "U kunt een nieuw wachtwoord instellen door op de onderstaande link te klikken of deze in uw browser te kopi\u00ebren.\n\n";
				body += url + "\n\n";
				body += "Met vriendelijke groet,\n";
				body += "Het Waisda? team.";

				message.setFrom(new InternetAddress(
						"noreply@beeldengeleuid.nl",
						"Waisda?"));
				message.setTo(reset.getUser().getEmail());
				message.setSubject("Verzoek om nieuw wachtwoord");
				message.setText(body);
			}
		};
		log.info(String.format(
				"Sending password reset mail to user %d, email %s",
				reset.getId(), reset.getUser().getEmail()));
		mailSender.send(preparator);
	}

}