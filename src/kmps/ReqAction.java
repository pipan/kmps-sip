package kmps;

import javax.sip.RequestEvent;

public interface ReqAction {

	public void reg(Controll ctr, RequestEvent e);
	public void auth(Controll ctr, RequestEvent e);
	
}
