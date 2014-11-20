package kmps;

import javax.sip.RequestEvent;

public class State implements ReqAction {
	
	protected Status value;
	
	public State(){
		value = Status.REG;
	}
	
	public void changeState(Controll ctr, RequestEvent e){
		System.out.println("<-- State changed from " + this.value.toString());
		switch(this.value){
		case REG:
			reg(ctr, e);
			break;
		case AUTH:
			auth(ctr, e);
			break;
		case ACK:
			break;
		}
		System.out.println(" to " + this.value.toString());
	}
	
	@Override
	public void reg(Controll ctr, RequestEvent req) {
		// TODO Auto-generated method stub
		value = Status.AUTH;
	}

	@Override
	public void auth(Controll ctr, RequestEvent req) {
		// TODO Auto-generated method stub
		value = Status.ACK;
	}

}
