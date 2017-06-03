package test;

import db.DataBaseUtil;

public class TlsPassword {

	public static void main(String[] args) {
		//DataBaseUtil.setTlsPassword("test123");
		char[] tlsPassword = DataBaseUtil.getTlsPassword();
		System.out.println("Password: ");
		for (char c : tlsPassword) {
			System.out.println(c);
		}
	}
}
