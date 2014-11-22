package kmps;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;

public class ConState implements ConnectionAction {
	
	protected ConStatus value;
	
	public ConState(){
		value = ConStatus.INVITE;
	}
	
	public void changeState(Controll ctr, RequestEvent e){
		System.out.println("<-- State changed from " + this.value.toString());
		switch(this.value){
		case INVITE:
			invite(ctr, e);
			break;
		case ACK:
			break;
		}
		System.out.println(" to " + this.value.toString());
	}
	public void changeState(Controll ctr, ResponseEvent e){
		System.out.println("<-- State changed from " + this.value.toString());
		switch(this.value){
		case RINGING:
			if (e.getResponse().getStatusCode() == 180){
				ringing(ctr, e);
			}
			break;
		case OK:
			if (e.getResponse().getStatusCode() == 200){
				ok(ctr, e);
			}
			break;
		}
		System.out.println(" to " + this.value.toString());
	}
	
	@Override
	public void invite(Controll ctr, RequestEvent e) {
		// TODO Auto-generated method stub
		value = ConStatus.RINGING;
	}

	@Override
	public void ringing(Controll ctr, ResponseEvent e) {
		// TODO Auto-generated method stub
		value = ConStatus.OK;
	}
	
	@Override
	public void ok(Controll ctr, ResponseEvent e) {
		// TODO Auto-generated method stub
		value = ConStatus.ACK;
	}
	
	@Override
	public void ack(Controll ctr, RequestEvent e) {
		// TODO Auto-generated method stub
		value = ConStatus.ACK;
	}
}
