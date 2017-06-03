package gui;

import javax.swing.*;

import client.Client;
import entity.ChatMessage;

import java.awt.*;
import java.awt.event.*;


public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField messageField;
	private JTextField serverField, portField;
	private JButton login, logout, whoIsIn;
	private JTextArea textArea;
	private boolean connected;
	private Client client;
	private int defaultPort;
	private String defaultHost;

	ClientGUI(String host, int port) {

		super("Chat Client");
		defaultPort = port;
		defaultHost = host;
		
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 3));
		serverField = new JTextField(host);
		portField = new JTextField("" + port);
		portField.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Server Address:  "));
		serverAndPort.add(serverField);
		serverAndPort.add(new JLabel("Port Number:  "));
		serverAndPort.add(portField);
		serverAndPort.add(new JLabel(""));
		northPanel.add(serverAndPort);

		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		northPanel.add(label);
		messageField = new JTextField("Anonymous");
		messageField.setBackground(Color.WHITE);
		northPanel.add(messageField);
		add(northPanel, BorderLayout.NORTH);

		textArea = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(textArea));
		textArea.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);

		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);
		whoIsIn = new JButton("Who is in");
		whoIsIn.addActionListener(this);
		whoIsIn.setEnabled(false);

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		messageField.requestFocus();

	}

	// method for client to append the text
	public void append(String str) {
		textArea.append(str);
		textArea.setCaretPosition(textArea.getText().length() - 1);
	}

	public void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
		label.setText("Enter your username below");
		messageField.setText("Anonymous");
		portField.setText("" + defaultPort);
		serverField.setText(defaultHost);
		serverField.setEditable(false);
		portField.setEditable(false);
		messageField.removeActionListener(this);
		connected = false;
	}
		
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == logout) {
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
			System.exit(0);
			return;
		}
		if(o == whoIsIn) {
			client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
			return;
		}
		if(connected) {
			client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, messageField.getText()));				
			messageField.setText("");
			return;
		}

		if(o == login) {
			String username = messageField.getText().trim();
			if(username.length() == 0)
				return;
			String server = serverField.getText().trim();
			if(server.length() == 0)
				return;
			String portNumber = portField.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;
			}

			client = new Client(server, port, username, this);
			if(!client.start()) 
				return;
			messageField.setText("");
			label.setText("Enter your message below");
			connected = true;
			
			login.setEnabled(false);
			logout.setEnabled(true);
			whoIsIn.setEnabled(true);
			serverField.setEditable(false);
			portField.setEditable(false);
			messageField.addActionListener(this);
		}

	}

	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);
	}

}

