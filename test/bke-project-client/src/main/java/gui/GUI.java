package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI implements Runnable {
	public static void main(String[] args) {
		EventQueue.invokeLater(new GUI());
	}

	@Override
	public void run() {
		JFrame f = new PasswordDialogFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Komunikator v1.0");
		f.setPreferredSize(new Dimension(400, 300));
		f.add(new MainPanel());
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}

class PasswordDialogFrame extends JFrame {
	private static final long serialVersionUID = 6546729298782148012L;
	private PasswordValidationDialog passDialog;

	public PasswordDialogFrame() {
		passDialog = new PasswordValidationDialog(this, true);
		passDialog.setVisible(true);
	}
}

class MainPanel extends JPanel {

	private static final long serialVersionUID = -5231455896370981513L;

	public MainPanel() {
		super(new BorderLayout());
		JLabel label = new JLabel("Rozmowa", JLabel.CENTER);
		JTextArea indicator = new JTextArea();
		InputPanel input = new InputPanel();
		this.add(label, BorderLayout.NORTH);
		this.add(indicator, BorderLayout.CENTER);
		this.add(input, BorderLayout.SOUTH);
	}
}

class InputPanel extends JPanel {

	private static final long serialVersionUID = -7859144114768884480L;

	public InputPanel() {
		super(new BorderLayout());
		SendButton send = new SendButton();
		JTextField text = new JTextField();
		
		send.setPreferredSize(new Dimension(100,30));
		text.setPreferredSize(new Dimension(300,30));
		
		this.add(text, BorderLayout.WEST);
		this.add(send, BorderLayout.EAST);
	}
}

class SendButton extends JButton {

	private static final long serialVersionUID = 4646034150300001172L;
	
	public SendButton() {
		this.setText("Wyœlij");
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Do Something Clicked");
			}
		});
	}
	
}
