package com.hb.hbsq.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import android.util.Log;

public class Md5 {

	public static String md5(String name) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffers = md.digest(name.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < buffers.length; i++) {
				String s = Integer.toHexString(0xff & buffers[i]);
				if (s.length() == 1) {
					sb.append("0" + s);
				}
				if (s.length() != 1) {
					sb.append(s);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final String TAG = "MD5";
	private static final String ALGORITHM = "MD5";

	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static MessageDigest sDigest;

	static {
		try {
			sDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "Get MD5 Digest failed.");
		}
	}

}
