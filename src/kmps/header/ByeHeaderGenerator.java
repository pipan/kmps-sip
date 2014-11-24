package kmps.header;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.SipException;
import javax.sip.address.SipURI;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

import kmps.Controll;
import kmps.Req;

public class ByeHeaderGenerator extends HeaderGenerator {

	public ByeHeaderGenerator() throws NoSuchAlgorithmException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Request generateBye(Request r, Req req, ViaHeader via, Controll ctr) throws NullPointerException, SipException, ParseException{
		
        ToHeader toHeader = (ToHeader) r.getHeader(ToHeader.NAME);
        r.addFirst(via);

        SipURI requsetUri = (SipURI) r.getRequestURI();
        requsetUri.setPort(req.getPort());
        requsetUri.setHost(req.getHost());

        SipURI thSipUri = (SipURI) toHeader.getAddress().getURI();
        thSipUri.setUser(req.getAccount().getName());
        thSipUri.setPort(ctr.getPort());
        toHeader.getAddress().setDisplayName(req.getAccount().getName());
        
		return r;
	}

}
