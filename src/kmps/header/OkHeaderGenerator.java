package kmps.header;

import gov.nist.core.InternalErrorHandler;
import gov.nist.javax.sip.header.AuthenticationHeader;
import gov.nist.javax.sip.header.Authorization;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.WWWAuthenticate;
import gov.nist.javax.sip.parser.WWWAuthenticateParser;

import java.security.NoSuchAlgorithmException;

import javax.sip.address.URI;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class OkHeaderGenerator extends HeaderGenerator {

	public OkHeaderGenerator() throws NoSuchAlgorithmException {
		super();
	}

	public void generateOk(HeaderFactory headerFactory, Response response, Integer expires){
        try {
            ExpiresHeader ok = headerFactory.createExpiresHeader(expires);
            response.setHeader(ok);
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
        }
    }
	
	public boolean isPassword(Request request, String hashedPassword) {
		Authorization authHeader = (Authorization)request.getHeader(Authorization.NAME);
		
        if ( authHeader == null ) return false;
        String realm = authHeader.getRealm();
        String username = authHeader.getUsername();
      
        if ( username == null || realm == null ) {
            return false;
        }
       
        String nonce = authHeader.getNonce();
        URI uri = authHeader.getURI();
        if (uri == null) {
            return false;
        }
        

      
        String A2 = request.getMethod().toUpperCase() + ":" + uri.toString();
        String HA1 = hashedPassword;

       
        byte[] mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = toHexString(mdbytes);
      
        String cnonce = authHeader.getCNonce();
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) {
            KD += ":" + cnonce;
        }
        KD += ":" + HA2;
        mdbytes = messageDigest.digest(KD.getBytes());
        String mdString = toHexString(mdbytes);
        String response = authHeader.getResponse();
       
        return mdString.equals(response);
    }
	
}
