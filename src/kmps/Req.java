package kmps;

import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import gov.nist.javax.sip.clientauthutils.DigestServerAuthenticationHelper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.header.ContactHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Response;

import kmps.header.HeaderGenerator;
import kmps.header.OkHeaderGenerator;
import kmps.header.RegistrationHeaderGenerator;
import kmps.header.contact.MyContactAddress;

public class Req extends State {
	
	private String host;
	private Integer port;
	private DigestServerAuthenticationHelper authHelper;
	private Account account;
	
	public Req(RequestEvent e){
		super();
		ViaHeader via = (ViaHeader)e.getRequest().getHeader("via");
		this.host = via.getHost();
		this.port = via.getPort();
		try {
			authHelper = new DigestServerAuthenticationHelper();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean equalsByReq(RequestEvent e){
		ViaHeader via = (ViaHeader)e.getRequest().getHeader("via");
		if (this.host.equals(via.getHost()) && this.port.equals(via.getPort())){
			return true;
		}
		return false;
	}
	
	@Override
	public void reg(Controll ctr, RequestEvent e) {
		try {
			RegistrationHeaderGenerator gen = new RegistrationHeaderGenerator();
			ContactHeader cHeader = (ContactHeader)e.getRequest().getHeader("contact");
			MyContactAddress cParser = new MyContactAddress(cHeader.getAddress().getURI());
			account = ctr.getAccount(cParser.getName());
            Response authResponse = ctr.messageFactory.createResponse(Response.UNAUTHORIZED, e.getRequest());
            gen.generateChallenge(ctr.headerFactory, authResponse, ctr.getIP());
            ctr.respond(e, authResponse);
            this.value = Status.AUTH;
        } catch (ParseException ex) {
            System.out.println("<-- ERR: cannot REG");
            this.value = Status.REG;
        } catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
        }
		
	}

	@Override
	public void auth(Controll ctr, RequestEvent e) {
		try {
			System.out.println("pass -> " + account.getPasswordHash() + " */***/* ");
			OkHeaderGenerator gen = new OkHeaderGenerator();
			if (gen.isPassword(e.getRequest(), account.getPasswordHash())){
				Response okResponse = ctr.messageFactory.createResponse(Response.OK, e.getRequest());
				gen.generateOk(ctr.headerFactory, okResponse, 3600);
				ctr.respond(e, okResponse);
				this.value = Status.ACK;
				
				/*
				ctr.headerFactory.create
				
				Request req = evt.getRequest();
				String method = req.getMethod();
				if( ! method.equals("MESSAGE")) {
					messageProcessor.processError("Bad request type: " + method);
					return;
				}
				FromHeader from = (FromHeader)req.getHeader("From");
				messageProcessor.processMessage(from.getAddress().toString(), new String(req.getRawContent()));
				Response response=null;
				try {
					response = messageFactory.createResponse(200, req);
					ToHeader toHeader = (ToHeader)response.getHeader(ToHeader.NAME);
					toHeader.setTag("888");
					ServerTransaction st = sipProvider.getNewServerTransaction(req);
					st.sendResponse(response);
				} catch (Throwable e) {
					e.printStackTrace();
					messageProcessor.processError("Can't send OK reply.");
				}
				*/
			}
			else{
				System.out.println("<-- WAR: Wrong Password");
			}
		} catch (ParseException e1) {
			System.out.println("<-- ERR: Auth Error");
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void ack(Controll ctr, RequestEvent e) {
		// TODO Auto-generated method stub
		this.value = Status.FIN;
	}
}
