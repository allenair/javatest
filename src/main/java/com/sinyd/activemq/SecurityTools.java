package com.sinyd.activemq;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;


public class SecurityTools {
	// keystore client path
	private static String keyStore = "/client.ks";

	// truststore client path
	private static String trustStore = "/client.ts";

	private static String keyStorePassword = "allenwang";

	public static TrustManager[] getTrustManagers()
			throws NoSuchAlgorithmException, KeyStoreException, IOException,
			GeneralSecurityException {

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(SecurityTools.class.getResource(trustStore).getFile()), null);
		TrustManagerFactory tmf = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);

		return tmf.getTrustManagers();
	}

	public static KeyManager[] getKeyManagers()
			throws NoSuchAlgorithmException, KeyStoreException,
			GeneralSecurityException, CertificateException, IOException,
			UnrecoverableKeyException {
System.out.println(SecurityTools.class.getResource("/"));
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(SecurityTools.class.getResource(keyStore).getFile()), keyStorePassword.toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm());
		kmf.init(ks, keyStorePassword.toCharArray());

		return kmf.getKeyManagers();
	}
}
