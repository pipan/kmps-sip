package kmps.header;

import gov.nist.core.InternalErrorHandler;

import java.security.NoSuchAlgorithmException;

import javax.sip.header.AuthorizationHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Response;

public class RegistrationHeaderGenerator extends HeaderGenerator{

	public RegistrationHeaderGenerator() throws NoSuchAlgorithmException {
		super();
	}
	
	public void generateChallenge(HeaderFactory headerFactory, Response response, String realm){
        try {
            WWWAuthenticateHeader auth = headerFactory.createWWWAuthenticateHeader(DEFAULT_SCHEME);
            auth.setParameter("realm", realm);
            auth.setParameter("nonce", generateNonce());
            auth.setParameter("algorithm", DEFAULT_ALGORITHM);
            response.setHeader(auth);
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
        }
    }
}
