package kmps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import kmps.gui.ServerWindow;

public class Controll {
	
	private String ip;
	private Integer port;
	private String protocol;
	private String username;
	
	private SipFactory sipFactory;
	private SipStack sipStack;
	protected SipProvider sipProvider;
	protected MessageFactory messageFactory;
	protected HeaderFactory headerFactory;
	private ContactHeader contactHeader;
	protected AddressFactory addressFactory;
	private Address contactAddress;
	private ListeningPoint listeningPoint;
	
	private Properties properties;
	
	private MySipListener listener;
	
	protected ReqList reqList;
	protected ConList conList;
	protected List<Account> accountList;
	
	private ServerWindow win;
	
	public Controll(ServerWindow win){
		this.win = win;
		listener = new MySipListener(this);
		accountList = new ArrayList<Account>();
		reqList = new ReqList(win);
		conList = new ConList(win);
	}
	
	public void start(){
		try {
			initAccount();
			initConfig();
			
			// Create the SIP factory and set the path name.
			this.sipFactory = SipFactory.getInstance();
			this.sipFactory.setPathName("gov.nist");
			// Create and set the SIP stack properties.
			this.properties = new Properties();
			this.properties.setProperty("javax.sip.STACK_NAME", this.ip);
			
			
			System.out.println(properties.toString());
			this.sipStack = this.sipFactory.createSipStack(this.properties);
			
			this.messageFactory = this.sipFactory.createMessageFactory();
			
			this.headerFactory = this.sipFactory.createHeaderFactory();
			
			this.addressFactory = this.sipFactory.createAddressFactory();
			
			this.listeningPoint = this.sipStack.createListeningPoint(this.ip, this.port, this.protocol);
			
			this.sipProvider = this.sipStack.createSipProvider(this.listeningPoint);
			this.sipProvider.addSipListener(listener);
			
			this.contactAddress = this.addressFactory.createAddress("sip:" + this.username + "@" + this.ip + ";transport=tcp");
			
			this.contactHeader = this.headerFactory.createContactHeader(contactAddress);
			
			System.out.println(this.ip);
			
		}
		catch (NullPointerException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void addAccount(Account a){
		accountList.add(a);
		win.addAccount(a);
	}
	public void removeAccount(Account a){
		accountList.remove(a);
		win.removeAccount(accountList);
	}
	
	public Account getAccount(String name){
		for(Account a : accountList){
			if (a.getExt().equals(name)){
				return a;
			}
		}
		return null;
	}
	public Integer getAccountId(Req r){
		Integer i = 0;
		for (Account a : accountList){
			if (a == r.getAccount()){
				break;
			}
			i++;
		}
		return i;
	}
	public Integer getConnectionId(Connection con){
		Integer i = 0;
		for (Connection c : conList.list){
			if (c.getCallId().equals(con.getCallId())){
				return i;
			}
			i++;
		}
		return i;
	}
	public String getIP(){
		return ip;
	}
	public Integer getPort(){
		return port;
	}
	public String getProtocol(){
		return protocol;
	}
	public List<Account> getAccounts(){
		return accountList;
	}
	
	public AddressFactory getAddressFactory(){
		return addressFactory;
	}
	public HeaderFactory getHeaderFactory(){
		return headerFactory;
	}
	
	public void respond(Response response, ServerTransaction sTransaction) {
        try {
            if (sTransaction != null) {
                try {
                	sTransaction.sendResponse(response);
                } catch (SipException | InvalidArgumentException ex) {
                    System.out.println("<-- ERR: Response1 Error");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
	public void respond(RequestEvent e, Response response) {
		ServerTransaction trans = this.getServerTransaction(e);
        try {
            if (trans == null) {
            	trans = this.sipProvider.getNewServerTransaction(e.getRequest());
            }
            trans.sendResponse(response);

        } catch (SipException | InvalidArgumentException ex) {
        	System.out.println("<-- ERR: Response2 Error");
        }
    }
	
	public void require(Request r){
		try {
			ClientTransaction trans = sipProvider.getNewClientTransaction(r);
			trans.sendRequest();
		} catch (TransactionUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SipException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	 public ServerTransaction getServerTransaction(RequestEvent e) {
        if (e.getServerTransaction() != null) {
            return e.getServerTransaction();
        } else {
            try {
                return this.sipProvider.getNewServerTransaction(e.getRequest());
            } catch (TransactionAlreadyExistsException ex) {
            	System.out.println("<-- EXC: transaction already exists");
            } catch (TransactionUnavailableException ex) {
                System.out.println("<-- EXC: transaction unavalible exception");
            }
        }
        return null;
    }
	 
	 public void connected(Req r){
		 win.connected(r);
	 }
	 
	 public void disconnected(Req s){
		 win.disconnected(s);
	 }
	 
	 public void callCreated(Connection c){
		 win.callCreated(c);
	 }
	 public void callAccepted(Connection c){
		 win.callAccepted(c);
		 System.out.println("ACCepted");
	 }
	 
	 public void initAccount() throws IOException{
		BufferedReader config = new BufferedReader(new FileReader("account.txt"));
		String line;
		String[] lineSplit;
		
		while ((line = config.readLine()) != null){
			lineSplit = line.split(": ");
			if (lineSplit.length > 1 && lineSplit[0].equals("ACCOUNT")){
				lineSplit = lineSplit[1].split(" ");
				if (lineSplit.length == 3){
					addAccount(new Account(lineSplit[0], lineSplit[2], lineSplit[1]));
				}
			}
			
		}
		config.close();
	 }
	 
	 public void initConfig() throws IOException{
		BufferedReader config = new BufferedReader(new FileReader("config.txt"));
		String line;
		String[] lineSplit;
		
		while ((line = config.readLine()) != null){
			lineSplit = line.split(": ");
			if (lineSplit.length > 1 && lineSplit[0].equals("IP")){
				this.ip = lineSplit[1];
			}
			else if (lineSplit.length > 1 && lineSplit[0].equals("PORT")){
				this.port = Integer.parseInt(lineSplit[1]);
			}
			else if (lineSplit.length > 1 && lineSplit[0].equals("PROTOCOL")){
				this.protocol = lineSplit[1];
			}
			
		}
		config.close();
	 }
}
