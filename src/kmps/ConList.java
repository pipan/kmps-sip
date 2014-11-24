package kmps;

import java.util.ArrayList;
import java.util.List;

import javax.sip.header.CallIdHeader;

import kmps.gui.ServerWindow;

public class ConList {

	protected List<Connection> list;
	private ServerWindow win;
	
	public ConList(ServerWindow win){
		this.list = new ArrayList<Connection>();
		this.win = win;
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
