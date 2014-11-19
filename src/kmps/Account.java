package kmps;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account {

	private String name;
	private String pass;
	private String ext;
	
	public Account(String name, String pass, String ext){
		this.name = name;
		this.pass = pass;
		this.ext = ext;
	}
	
	public String getExt(){
		return ext;
	}
	public String getName(){
		return name;
	}
	public String getPassword(){
		return pass;
	}
	public String getPasswordHash() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] tmp = pass.getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] ret = md.digest(tmp);
		return ret.toString();
	}
}
