package kmps;

import java.util.LinkedList;
import java.util.List;

import javax.sip.RequestEvent;
import javax.swing.text.html.HTMLDocument.Iterator;

public class ReqList {
	
	private List<Req> list;
	
	public ReqList(){
		list = new LinkedList<Req>();
	}
	
	public void add(Req r){
		list.add(r);
	}
	
	public Req getByEvent(RequestEvent e){
		for (Req r : list){
			if (r.equalsByReq(e)){
				return r;
			}
		}
		return null;
	}
	
	public boolean existsByEvent(RequestEvent e){
		for (Req r : list){
			if (r.equalsByReq(e)){
				return true;
			}
		}
		return false;
	}
}
