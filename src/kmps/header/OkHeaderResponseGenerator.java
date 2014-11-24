package kmps.header;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import kmps.Controll;
import kmps.Req;

public class OkHeaderResponseGenerator extends HeaderGenerator {

	
	public OkHeaderResponseGenerator() throws NoSuchAlgorithmException {
		super();
		// TODO Auto-generated constructor stub
	}

	public Response generateOkResponse(Response r, Req req, ViaHeader via, Controll ctr) throws ParseException, NullPointerException, SipException{
		r.removeHeader(ViaHeader.NAME);
        r.addFirst(via);

        SipURI uri = (SipURI) ctr.getAddressFactory().createSipURI(req.getAccount().getExt().toString(), ctr.getIP());
        uri.setTransportParam(ctr.getProtocol());
        uri.setPort(ctr.getPort());
        
        Address address = ctr.getAddressFactory().createAddress(req.getAccount().getExt().toString(), uri);
        address.setDisplayName(req.getAccount().getName());
        
        ContactHeader contactHeader = ctr.getHeaderFactory().createContactHeader(address);
        r.setHeader(contactHeader);
		
		return r;
	}
}
