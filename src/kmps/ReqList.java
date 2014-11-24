package kmps;

import java.util.LinkedList;
import java.util.List;

import javax.sip.RequestEvent;

import kmps.gui.ServerWindow;

public class ReqList {
	
	private List<Req> list;
	private ServerWindow win;
	
	public ReqList(ServerWindow win){
		list = new LinkedList<Req>();
		this.win = win;
	}
	
	public void add(Req r){
		list.add(r);
	}
	
	public Req getByEvent(RequestEvent e){
		for (Req r : list){
			if (r.equalsByEvent(e)){
				return r;
			}
		}
		return null;
	}
	public Req getByLocation(RequestEvent e){
		for (Req r : list){
			if (r.equalsByLocation(e)){
				return r;
			}
		}
		return null;
	}
	public Req getByExt(String ext){
		for (Req r : list){
			if (r.equalsByExt(ext)){
				return r;
			}
		}
		return null;
	}
	
	public void removeByEvent(RequestEvent e, boolean finnished){
		for (Req r : list){
			if (r.equalsByEvent(e) && (!finnished || r.value == Status.ACK)){
				list.remove(r);
			}
		}
	}
	public void removeByAccount(Account a, boolean finnished){
		for (Req r : list){
			if (r.equalsByAccount(a) && (!finnished || r.value == Status.ACK)){
				list.remove(r);
			}
		}
	}
	
	public boolean existsByEvent(RequestEvent e, boolean finnished){
		for (Req r : list){
			if (r.equalsByEvent(e) && (!finnished || r.value == Status.ACK)){
				return true;
			}
		}
		return false;
	}
	public boolean existsByLocation(RequestEvent e, boolean finnished){
		for (Req r : list){
			if (r.equalsByLocation(e) && (!finnished || r.value == Status.ACK)){
				return true;
			}
		}
		return false;
	}
	public boolean existsByAccount(Account a, boolean finnished){
		for (Req r : list){
			if (r.equalsByAccount(a) && (!finnished || r.value == Status.ACK)){
				return true;
			}
		}
		return false;
	}
}
