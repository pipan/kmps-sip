package kmps;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import kmps.header.InviteHeaderGenerator;
import kmps.header.OkHeaderResponseGenerator;

public class Connection extends ConState {

	private Req collee;
	private Req coller;
	private CallIdHeader id;
	private RequestEvent requestInvite;
	private ServerTransaction trans;
	
	public Connection(Req coller, Req collee, CallIdHeader id){
		this.collee = collee;
		this.coller = coller;
		this.id = id;
	}
	
	@Override
	public void invite(Controll ctr, RequestEvent e) {
		/*
		CallIdHeader id = (CallIdHeader) e.getRequest().getHeader(CallIdHeader.NAME);
		Req collee = ctr.conList.getById(id).collee;
		*/
		
		ViaHeader via;
		try {
			this.requestInvite = e;
			this.trans = ctr.getServerTransaction(e);
			//TRYING RESPONS
			ctr.respond(e, ctr.messageFactory.createResponse(Response.TRYING, e.getRequest()));
			via = ctr.headerFactory.createViaHeader(ctr.getIP(), ctr.getPort(), ctr.getProtocol(), ((ViaHeader)e.getRequest().getHeader("via")).getBranch());
			InviteHeaderGenerator gen = new InviteHeaderGenerator();
			Request tmp = gen.generateInvite((Request)e.getRequest().clone(), this.collee, via, ctr);
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
	public void ringing(Controll ctr, ResponseEvent e) {
		Response res;
		try {
			res = ctr.messageFactory.createResponse(Response.RINGING, this.requestInvite.getRequest());
			ctr.respond(res, trans);
			value = ConStatus.OK;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	@Override
	public void ok(Controll ctr, ResponseEvent e) {
		try {
			ViaHeader via = (ViaHeader) requestInvite.getRequest().getHeader(ViaHeader.NAME);
			OkHeaderResponseGenerator gen = new OkHeaderResponseGenerator();
			Response tmp = gen.generateInvite((Response)e.getResponse().clone(), this.coller, via, ctr);
			ctr.respond(tmp, trans);
			value = ConStatus.ACK;
		} catch (NullPointerException | ParseException | SipException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	@Override
	public void ack(Controll ctr, RequestEvent e) {
		// TODO Auto-generated method stub
		value = ConStatus.ACK;
	}
	
	public boolean equalsById(CallIdHeader id){
		return this.id.equals(id);
	}
}
