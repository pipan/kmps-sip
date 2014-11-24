package kmps;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;

public interface ConnectionAction {

	public void invite(Controll ctr, RequestEvent e);
	public void ringing(Controll ctr, ResponseEvent e);
	public void ok(Controll ctr, ResponseEvent e);
	public void ack(Controll ctr, RequestEvent e);
	public void bye(Controll ctr, RequestEvent e);
}
