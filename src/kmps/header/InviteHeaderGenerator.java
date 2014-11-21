package kmps.header;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.Header;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

import kmps.Account;
import kmps.Controll;
import kmps.Req;

public class InviteHeaderGenerator extends HeaderGenerator {

	public InviteHeaderGenerator() throws NoSuchAlgorithmException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Request generateInvite(Request r, Req req, ViaHeader via, Controll ctr) throws ParseException, NullPointerException, SipException{
		r.addFirst(via);
		
		SipURI uri = (SipURI)r.getRequestURI();
		uri.setHost(req.getHost());
		uri.setPort(req.getPort());
		
		ToHeader to = (ToHeader) r.getHeader(ToHeader.NAME);
		SipURI toUri = (SipURI) to.getAddress().getURI();
		toUri.setUser(req.getAccount().getName());
		toUri.setPort(ctr.getPort());
		to.getAddress().setDisplayName(req.getAccount().getName());
		/*
		request.addHeader(r.getHeader("callIdHeader"));
		request.addHeader(r.getHeader("cSeqHeader"));
		request.addHeader(r.getHeader("fromHeader"));
		request.addHeader(r.getHeader("toHeader"));
		request.addHeader(r.getHeader("maxForwardsHeader"));
		request.addHeader(r.getHeader("viaHeader"));
		request.addHeader(r.getHeader("contactHeader"));
		*/
		System.out.println("MY INVITE: " + r.toString());
		return r;
	}
}
