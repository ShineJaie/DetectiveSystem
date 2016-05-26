package net.system.utils;

import java.security.MessageDigest;

import org.junit.Test;

import sun.misc.BASE64Encoder;

public class MD5Utils {

	public static String encode(String str) {
		String result;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] hash = messageDigest.digest(str.getBytes("utf-8"));
			BASE64Encoder encoder = new BASE64Encoder();
			result = encoder.encode(hash);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Test
	public void testName() throws Exception {
		System.out.println(encode("HELLO"));
	}
}
