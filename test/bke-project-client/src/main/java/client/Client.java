package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import server.Protocol;

public class Client {

	public static void main(String[] args) throws Exception {
		char[] password = { 't', 'e', 's', 't', '1', '2', '3'};
		KeyStore ks = KeyStore.getInstance("PKCS12");
		try (InputStream input = new FileInputStream("client.pfx")) {
			ks.load(input, password);
		}

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(ks);
		TrustManager[] trustManagers = tmf.getTrustManagers();

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustManagers, null);

		SSLSocketFactory factory = sslContext.getSocketFactory();
		Protocol protocol = new Protocol();
		try (Socket socket = factory.createSocket("localhost", 12345)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			//String loginCommand = protocol.buildLoginCommand("test", "test");
			writer.write("#LOGIN#test;test");
/*			String response = reader.readLine();
			if(response.equals(Boolean.TRUE.toString())) {
				writer.write(protocol.buildMessageCommand("siema"));
			} else {
				throw new RuntimeException("Client disconected ! Invalid authorization");
			}*/
			writer.flush();

		}
	}

}
