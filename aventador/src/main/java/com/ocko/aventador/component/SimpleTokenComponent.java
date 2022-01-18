/**
 * 
 */
package com.ocko.aventador.component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * @author ok
 *
 */
@Component
public class SimpleTokenComponent {

	public String generatorToken() {
		try {
			UUID uuid = UUID.randomUUID();
					
			ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
	        long date = zdt.toInstant().toEpochMilli();
	        
	        String uniqueKey = uuid.toString() + date;
	        		
			MessageDigest salt = MessageDigest.getInstance("SHA-256");
			salt.update(uniqueKey.getBytes(StandardCharsets.UTF_8));

			final StringBuilder builder = new StringBuilder();
			for (final byte b : salt.digest()) {
				builder.append(String.format("%02x", b));
			}
			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
