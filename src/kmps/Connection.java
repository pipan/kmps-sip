package kmps;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

import kmps.header.InviteHeaderGenerator;

public class Connection extends ConState {

	private Req collee;
	private Req coller;
	private CallIdHeader id;
	
	public Connection(Req coller, Req collee, CallIdHeader id){
		this.collee = collee;
		this.coller = coller;
		this.id = id;
	}
	
	@Override
	public void invite(Controll ctr, RequestEvent e) {
		CallIdHeader id = (CallIdHeader) e.getRequest().getHeader(CallIdHeader.NAME);
		Req collee = ctr.conList.getById(id).collee;
		ViaHeader via;
		try {
			via = ctr.headerFactory.createViaHeader(ctr.getIP(), ctr.getPort(), ctr.getProtocol(), ((ViaHeader)e.getRequest().getHeader("via")).getBranch());
			InviteHeaderGenerator gen = new InviteHeaderGenerator();
			Request tmp = gen.generateInvite((Request)e.getRequest().clone(), collee, via, ctr);
			ctr.require(tmp);
			value = ConStatus.RINGING;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SipException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void ringing(Controll ctr, RequestEvent e) {
		
		value = ConStatus.ACK;
	}
	
	public boolean equalsById(CallIdHeader id){
		return this.id.equals(id);
	}
}
