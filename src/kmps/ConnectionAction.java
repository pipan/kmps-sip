package kmps;

import javax.sip.RequestEvent;

public interface ConnectionAction {

	public void invite(Controll ctr, RequestEvent e);
	public void ringing(Controll ctr, RequestEvent e);
}
