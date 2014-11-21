package kmps;

import javax.sip.RequestEvent;

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
		case RINGING:
			ringing(ctr, e);
			break;
		case ACK:
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
	public void ringing(Controll ctr, RequestEvent e) {
		// TODO Auto-generated method stub
		value = ConStatus.ACK;
	}
}
