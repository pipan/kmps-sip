package kmps;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.sip.RequestEvent;
import javax.sip.header.Header;
import javax.sip.header.ViaHeader;
import javax.sip.message.Response;

import kmps.header.OkHeaderGenerator;
import kmps.header.RegistrationHeaderGenerator;
import kmps.header.contact.MyContactAddress;

public class Req extends State {
	
	private String host;
	private Integer port;
	private Account account;
	
	public Req(RequestEvent e, Account account){
		super();
		ViaHeader via = (ViaHeader)e.getRequest().getHeader("via");
		this.host = via.getHost();
		this.port = via.getPort();
		this.account = account;
	}
	
	public void init(Account account){
		this.value = Status.REG; 
		this.account = account;
	}
	
	public String getHost(){
		return host;
	}
	public Integer getPort(){
		return port;
	}
	public Account getAccount(){
		return account;
	}
	
	@Override
	public void reg(Controll ctr, RequestEvent e) {
		try {
			//UNAUTHORIZED RESPONSE
			RegistrationHeaderGenerator gen = new RegistrationHeaderGenerator();
            Response authResponse = ctr.messageFactory.createResponse(Response.UNAUTHORIZED, e.getRequest());
            gen.generateChallenge(ctr.headerFactory, authResponse, ctr.getIP());
            ctr.respond(e, authResponse);
            this.value = Status.AUTH;
        } catch (ParseException ex) {
            System.out.println("<-- ERR: cannot REG");
        } catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
        }
		
	}

	@Override
	public void auth(Controll ctr, RequestEvent e) {
		try {
			System.out.println("pass -> " + account.getPassword() + " */***/* ");
			OkHeaderGenerator gen = new OkHeaderGenerator();
			if (gen.isPasswordPlain(e.getRequest(), account.getPassword())){
				Response okResponse = ctr.messageFactory.createResponse(Response.OK, e.getRequest());
				gen.generateOk(ctr.headerFactory, okResponse, 3600);
				ctr.respond(e, okResponse);
				ctr.reqList.removeByAccount(account, true);
				this.value = Status.ACK;
			}
			else{
				System.out.println("<-- WAR: Wrong Password");
				Response wrongPasswordResponse = ctr.messageFactory.createResponse(Response.FORBIDDEN, e.getRequest());
				ctr.respond(e, wrongPasswordResponse);
				this.value = Status.REG;
			}
		} catch (ParseException e1) {
			System.out.println("<-- ERR: Auth Error");
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public boolean equalsByEvent(RequestEvent e){
		MyContactAddress cParser = MyContactAddress.getContactAddressByEvent(e);
		if (equalsByLocation(e) && equalsByExt(cParser.getName())){
			return true;
		}
		return false;
	}
	public boolean equalsByExt(String ext){
		if (this.account.getExt().equals(ext)){
			return true;
		}
		return false;
	}
	public boolean equalsByLocation(RequestEvent e){
		ViaHeader via = (ViaHeader)e.getRequest().getHeader("via");
		if (this.host.equals(via.getHost()) && this.port.equals(via.getPort())){
			return true;
		}
		return false;
	}
	public boolean equalsByAccount(Account a){
		if (account == a){
			return true;
		}
		return false;
	}
}
