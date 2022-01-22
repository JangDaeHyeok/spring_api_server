package jdh.test.api.util.encrypt;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyComponent {
	
	private static PublicKey publicKey = null;
	private static PrivateKey privateKey = null;
	
	public static PublicKey getPublicKey() {
		return publicKey;
	}
	public static void setPublicKey(PublicKey publicKey) {
		KeyComponent.publicKey = publicKey;
	}
	public static PrivateKey getPrivateKey() {
		return privateKey;
	}
	public static void setPrivateKey(PrivateKey privateKey) {
		KeyComponent.privateKey = privateKey;
	}
}
