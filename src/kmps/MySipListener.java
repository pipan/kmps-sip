package kmps;

import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.message.Request;

public class MySipListener implements SipListener {

	private Controll ctr;
	
	public MySipListener(Controll ctr){
		this.ctr = ctr;
	}
	
	@Override
	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processRequest(RequestEvent e) {
		Req req;
		//System.out.println(req.toString());
        if (e.getRequest().getMethod().equals(Request.REGISTER)) {
        	//System.out.println("REQUEST");
        	if (ctr.reqList.existsByEvent(e)){
        		System.out.println("<-- Request Exists");
        		Req tmp = ctr.reqList.getByEvent(e);
        		tmp.changeState(ctr, e);
        	}
        	else{
        		System.out.println("<-- Creating Request");
        		ctr.reqList.add(new Req(e));
        	}
        	/*
            reg = findRegistration(req);
            if (reg != null) {
                System.out.println(reg);
                reg.register(req);
            } else {
                System.out.println("reg not found");
                reg = new Registration(sipServer);
                sipServer.getRegistrationList().add(reg);
                reg.register(requestEvent);

            }
            */
        }
        if (e.getRequest().getMethod().equals(Request.INVITE)) {
        	System.out.println("INVITE");
        	/*
            System.out.println(requestEvent.getRequest().toString());
            reg = findRegistration(requestEvent);
            if (reg != null) {
                reg.createCall(requestEvent);
            } else {
                System.out.println("neexistuje zariadenie");
            }
            */
        }
		
	}

	@Override
	public void processResponse(ResponseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
