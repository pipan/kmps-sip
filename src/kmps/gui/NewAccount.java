package kmps.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import kmps.Account;
import kmps.Controll;

public class NewAccount extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField extField;
	private JPasswordField passField;
	
	private JFrame frame;

	/**
	 * Create the frame.
	 */
	public NewAccount(final Controll ctr) {
		frame = this;
		setTitle("Novy Ucet");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 225, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nameField = new JTextField();
		nameField.setBounds(33, 28, 128, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		extField = new JTextField();
		extField.setBounds(33, 74, 128, 20);
		contentPane.add(extField);
		extField.setColumns(10);
		
		passField = new JPasswordField();
		passField.setBounds(32, 118, 129, 20);
		contentPane.add(passField);
		
		JLabel lblMeno = new JLabel("Meno");
		lblMeno.setBounds(33, 11, 46, 14);
		contentPane.add(lblMeno);
		
		JLabel lblPredpona = new JLabel("Linka");
		lblPredpona.setBounds(33, 59, 46, 14);
		contentPane.add(lblPredpona);
		
		JLabel lblNewLabel = new JLabel("Heslo");
		lblNewLabel.setBounds(33, 105, 46, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("vytvor");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ctr.addAccount(new Account(nameField.getText(), passField.getText(), extField.getText()));
				frame.dispose();
			}
		});
		btnNewButton.setBounds(33, 149, 128, 23);
		contentPane.add(btnNewButton);
		
		setVisible(true);
	}
}
