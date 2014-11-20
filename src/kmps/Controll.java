package kmps;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import javax.sip.message.Response;

public class Controll {
	
	private String ip;
	private Integer port;
	private String protocol;
	private String username;
	
	private SipFactory sipFactory;
	private SipStack sipStack;
	private SipProvider sipProvider;
	protected MessageFactory messageFactory;
	protected HeaderFactory headerFactory;
	private ContactHeader contactHeader;
	private AddressFactory addressFactory;
	private Address contactAddress;
	private ListeningPoint listeningPoint;
	
	private Properties properties;
	
	private MySipListener listener;
	
	protected ReqList reqList;
	protected List<Account> accountList;
	
	public Controll(){
		listener = new MySipListener(this);
		accountList = new ArrayList<Account>();
		accountList.add(new Account("Feri", "Feri101", "101"));
		accountList.add(new Account("Karol", "Karol102", "102"));
		reqList = new ReqList();
		this.protocol = "UDP";
		this.port = 5060;
	}
	
	public void start(){
		
		try {
			// Get the local IP address.
			//this.ip = "147.175.182.134";
			this.ip = "192.168.104.1";
			//this.ip = "192.168.0.111";
			
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
	
	public Account getAccount(String name){
		for(Account a : accountList){
			if (a.getExt().equals(name)){
				return a;
			}
		}
		return null;
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
	
	public String getIP(){
		return this.ip;
	}
}
