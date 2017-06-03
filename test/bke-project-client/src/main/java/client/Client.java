package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import db.DataBaseUtil;
import entity.ChatMessage;
import gui.ClientGUI;

public class Client {

	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	private Socket socket;

	private ClientGUI clientGui;

	private String server, username;
	private int port;

	public Client(String server, int port, String username) {
		this(server, port, username, null);
	}

	public Client(String server, int port, String username, ClientGUI cg) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.clientGui = cg;
	}

	public boolean start() {
		try {
			initializeClientSocket();
		} catch (Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);

		try {
			sInput = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		new ListenFromServer().start();
		try {
			sOutput.writeObject(username);
		} catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		return true;
	}

	private void initializeClientSocket() throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, KeyManagementException, UnknownHostException {
		char[] password = DataBaseUtil.getTlsPassword();
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
		socket = factory.createSocket(server, port);
	}

	private void display(String msg) {
		if (clientGui == null)
			System.out.println(msg);
		else
			clientGui.append(msg + "\n");
	}

	public void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
			if(msg.getType() == ChatMessage.LOGOUT) {
				disconnect();
			}
		} catch (IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	private void disconnect() {
		try {
			if (sInput != null)
				sInput.close();
		} catch (Exception e) {
		}
		try {
			if (sOutput != null)
				sOutput.close();
		} catch (Exception e) {
		}
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		}
		if (clientGui != null)
			clientGui.connectionFailed();

	}

	public static void main(String[] args) {
		int portNumber = 1500;
		String serverAddress = "localhost";
		String userName = "Anonymous";

		switch (args.length) {
		case 3:
			serverAddress = args[2];
		case 2:
			try {
				portNumber = Integer.parseInt(args[1]);
			} catch (Exception e) {
				System.out.println("Invalid port number.");
				System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
				return;
			}
		case 1:
			userName = args[0];
		case 0:
			break;
		default:
			System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
		}
		Client client = new Client(serverAddress, portNumber, userName);
		if (!client.start())
			return;

		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			String msg = scan.nextLine();
			if (msg.equalsIgnoreCase("LOGOUT")) {
				client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
				break;
			}
			else if (msg.equalsIgnoreCase("WHOISIN")) {
				client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
			} else {
				client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
			}
		}
		scan.close();
		client.disconnect();
	}

	class ListenFromServer extends Thread {

		public void run() {
			while (true) {
				try {
					String msg;
					if((msg = (String)sInput.readObject()) != null) {
						if (clientGui == null) {
							System.out.println(msg);
							System.out.print("> ");
						} else {
							clientGui.append(msg);
						}
					}
				} catch (Exception e) {
					display("Server has close the connection.");
					if (clientGui != null)
						clientGui.connectionFailed();
					break;
				}
			}
		}
	}
}
