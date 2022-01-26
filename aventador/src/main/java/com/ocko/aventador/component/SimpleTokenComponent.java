/**
 * 
 */
package com.ocko.aventador.component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * @author ok
 *
 */
@Component
public class SimpleTokenComponent {

	public String generatorToken(Integer memberId) {
		UUID uuid = UUID.randomUUID();
					
		ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        long dateTime = zdt.toInstant().toEpochMilli();

        return uuidToBase64(uuid.toString()) + memberId;
	}
	
	private String uuidToBase64(String str) {
	    Base64 base64 = new Base64();
	    UUID uuid = UUID.fromString(str);
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    return base64.encodeBase64URLSafeString(bb.array());
	}
}
