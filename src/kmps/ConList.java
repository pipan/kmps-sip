package kmps;

import java.util.ArrayList;
import java.util.List;

import javax.sip.header.CallIdHeader;

public class ConList {

	List<Connection> list;
	
	public ConList(){
		this.list = new ArrayList<Connection>();
	}
	
	public void add(Connection c){
		list.add(c);
	}
	
	public Connection getById(CallIdHeader id){
		for (Connection c : list){
			if (c.equalsById(id)){
				return c;
			}
		}
		return null;
	}
	
	public boolean exists(CallIdHeader id){
		for (Connection c : list){
			if (c.equalsById(id)){
				return true;
			}
		}
		return false;
	}
}
