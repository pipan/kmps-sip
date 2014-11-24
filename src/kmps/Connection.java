package kmps;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import kmps.header.ByeHeaderGenerator;
import kmps.header.InviteHeaderGenerator;
import kmps.header.OkHeaderResponseGenerator;

public class Connection extends ConState {

	private Req callee;
	private Req caller;
	private CallIdHeader id;
	private RequestEvent requestInvite;
	private ServerTransaction trans;
	private ClientTransaction cTrans;
	
	public Connection(Req caller, Req callee, CallIdHeader id){
		this.callee = callee;
		this.caller = caller;
		this.id = id;
	}
	
	public Req getCaller(){
		return caller;
	}
	public Req getCallee(){
		return callee;
	}
	public CallIdHeader getCallId(){
		return id;
	}
	
	@Override
	public void invite(Controll ctr, RequestEvent e) {
		
		ViaHeader via;
		try {
			this.requestInvite = e;
			this.trans = ctr.getServerTransaction(e);
			//TRYING RESPONS
			ctr.respond(e, ctr.messageFactory.createResponse(Response.TRYING, e.getRequest()));
			via = ctr.headerFactory.createViaHeader(ctr.getIP(), ctr.getPort(), ctr.getProtocol(), ((ViaHeader)e.getRequest().getHeader("via")).getBranch());
			InviteHeaderGenerator gen = new InviteHeaderGenerator();
			Request tmp = gen.generateInvite((Request)e.getRequest().clone(), this.callee, via, ctr);
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
			ctr.callCreated(this);
			value = ConStatus.OK;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	@Override
	public void ok(Controll ctr, ResponseEvent e) {
		try {
			cTrans = e.getClientTransaction();
			ViaHeader via = (ViaHeader) requestInvite.getRequest().getHeader(ViaHeader.NAME);
			OkHeaderResponseGenerator gen = new OkHeaderResponseGenerator();
			Response tmp = gen.generateOkResponse((Response)e.getResponse().clone(), this.caller, via, ctr);
			ctr.respond(tmp, trans);
			ctr.callAccepted(this);
			value = ConStatus.BYE;
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
		try {
			Request ack = cTrans.getDialog().createAck(((CSeqHeader) e.getRequest().getHeader(CSeqHeader.NAME)).getSeqNumber());
            cTrans.getDialog().sendAck(ack);
			value = ConStatus.BYE;
		} catch (InvalidArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SipException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void bye(Controll ctr, RequestEvent e) {
		try {
			//SEND OK
			Response ok = ctr.messageFactory.createResponse(Response.OK, e.getRequest());
			ctr.respond(e, ok);
			
			ByeHeaderGenerator gen = new ByeHeaderGenerator();
			
			ViaHeader via = (ViaHeader) e.getRequest().getHeader(ViaHeader.NAME);
            // request.removeHeader(ViaHeader.NAME);
            via = ctr.headerFactory.createViaHeader(ctr.getIP(), ctr.getPort(), ctr.getProtocol(), "z9hG4bK" + Long.toString(new Date().getTime()));
            //kto zrusil hovor
            SipURI uri = (SipURI)((ContactHeader)e.getRequest().getHeader(ContactHeader.NAME)).getAddress().getURI();
            Request tmp;
            if (ctr.getAccount(uri.getUser()) == this.callee.getAccount()){
            	tmp = gen.generateBye((Request)e.getRequest().clone(), caller, via, ctr);
            }
            else{
            	tmp = gen.generateBye((Request)e.getRequest().clone(), callee, via, ctr);
            }
			ClientTransaction ct = ctr.sipProvider.getNewClientTransaction(tmp);

	        cTrans.getDialog().sendRequest(ct);
			value = ConStatus.FIN;
		} catch (InvalidArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SipException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public boolean equalsById(CallIdHeader id){
		return this.id.equals(id);
	}
}
