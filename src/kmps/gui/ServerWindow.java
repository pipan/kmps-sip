package kmps.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;

import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JLabel;

import java.awt.Dimension;

import kmps.Account;
import kmps.Connection;
import kmps.Controll;
import kmps.Req;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ServerWindow extends JFrame {
	
	private JPanel contentPane;
	private JPanel accountPanel;
	
	private Integer accountTop = 1;
	
	private Controll ctr;
	private MyTable table;

	/**
	 * Create the frame.
	 */
	public ServerWindow(String title) {
		super(title);
		ctr = new Controll(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "name_26536688032898");
		
		JSplitPane accountPane = new JSplitPane();
		tabbedPane.addTab("ucty", null, accountPane, null);
		
		accountPanel = new JPanel();
		accountPanel.setPreferredSize(new Dimension(300, 10));
		accountPane.setLeftComponent(accountPanel);
		accountPanel.setLayout(null);
		
		JLabel lblUcty = new JLabel("ucty:");
		lblUcty.setBounds(10, 10, 46, 15);
		accountPanel.add(lblUcty);
		
		JPanel accountOperationPanel = new JPanel();
		accountPane.setRightComponent(accountOperationPanel);
		accountOperationPanel.setLayout(null);
		
		JLabel lblNovyUcet = new JLabel("novy ucet");
		lblNovyUcet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new NewAccount(ctr);
			}
		});
		lblNovyUcet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNovyUcet.setBounds(10, 11, 87, 14);
		accountOperationPanel.add(lblNovyUcet);
		
		JScrollPane conectionPane = new JScrollPane();
		tabbedPane.addTab("pripojenia", null, conectionPane, null);
		
		table = new MyTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Volajuci", "Volany", "Zaciatok", "Dlzka", "Prebieha"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		conectionPane.setViewportView(table);
		
		setVisible(true);
		ctr.start();
	}
	
	public void addAccount(Account a){
		accountPanel.add(new AccountPanel(ctr, a, accountTop));
		accountTop++;
		this.repaint();
	}
	public void removeAccount(List<Account> list){
		accountPanel.removeAll();
		accountTop = 1;
		JLabel lblUcty = new JLabel("ucty:");
		lblUcty.setBounds(10, 10, 46, 15);
		accountPanel.add(lblUcty);
		for(Account a : list){
			addAccount(a);
		}
		this.repaint();
	}
	
	public void connected(Req r){
		Integer i = ctr.getAccountId(r) + 1;
		((AccountPanel)accountPanel.getComponent(i)).setIP(r.getHost());
	}
	
	public void disconnected(Req r){
		Integer i = ctr.getAccountId(r) + 1;
		((AccountPanel)accountPanel.getComponent(i)).reset();
	}
	
	public void callCreated(Connection c){
		table.addRow(new MyRow(new Object[]{c.getCaller().getAccount().getExt(), c.getCallee().getAccount().getExt(), "0", "00:00", "RINGING"}));
	}
	public void callAccepted(Connection c){
		Integer i = ctr.getConnectionId(c) + 1;
		table.list.get(i).setStart(System.currentTimeMillis());
	}
}
