package kmps.header.contact;

import javax.sip.RequestEvent;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;

public class MyContactAddress {

	private URI address;
	private String name;
	private String ip;
	private Integer port;
	
	public MyContactAddress(URI address){
		this.address = address;
		parse();
	}
	
	public void setAddress(URI address){
		this.address = address;
		parse();
	}
	
	public void parse(){
		String[] tmp = address.toString().split(";");
		String[] half = tmp[0].split("@");
		tmp = half[0].split(":");
		this.name = tmp[1];
		tmp = half[1].split(":");
		this.ip = tmp[0];
		this.port = Integer.getInteger(tmp[1]);
	}
	
	public String getName(){
		return name;
	}
	public String getIP(){
		return ip;
	}
	public Integer getPort(){
		return port;
	}
	
	public static MyContactAddress getContactAddressByEvent(RequestEvent e){
		ContactHeader cHeader = (ContactHeader)e.getRequest().getHeader("contact");
		MyContactAddress cParser = new MyContactAddress(cHeader.getAddress().getURI());
		return cParser;
	}
}
