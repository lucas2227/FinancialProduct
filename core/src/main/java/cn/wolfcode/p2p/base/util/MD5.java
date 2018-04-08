package cn.wolfcode.p2p.base.util;

import java.security.MessageDigest;

public class MD5 {
	private MD5() {}
	
	public static String encode(String info) {
		try {
			StringBuilder sb = new StringBuilder(38);
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(info.getBytes("UTF-8"));
			for (byte b : data) {
				int temp = b & 255;
				if (temp < 16 && temp >= 0) {
					sb.append("0").append(Integer.toHexString(temp));
				} else {
					sb.append(Integer.toHexString(temp));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		System.out.println(encode("admin"+"123"));
		System.out.println(encode("xiaoyao"+"123"));
	}
}
