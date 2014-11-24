package kmps.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import kmps.Account;
import kmps.Controll;

public class AccountPanel extends JPanel {

	private JLabel ip;
	
	private JPanel panel;
	
	public AccountPanel(final Controll ctr, final Account a, Integer i){
		super();
		this.panel = this;
		
		setLayout(null);
		this.setBounds(10, 10 + (i * 20), 280, 15);
		
		JLabel ext = new JLabel(a.getExt());
		ext.setBounds(0, 0, 30, 15);
		this.add(ext);
		
		JLabel name = new JLabel(a.getName());
		name.setBounds(40, 0, 60, 15);
		this.add(name);
		
		ip = new JLabel();
		ip.setBounds(110, 0, 130, 15);
		this.add(ip);
		
		JLabel del = new JLabel("del");
		del.setBounds(250, 0, 30, 15);
		del.setCursor(new Cursor(Cursor.HAND_CURSOR));
		del.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				ctr.removeAccount(a);
			}
		});
		this.add(del);
	}
	
	public void setIP(String ip){
		this.ip.setText(ip);
	}
	public void reset(){
		this.ip.setText("");
	}
}
