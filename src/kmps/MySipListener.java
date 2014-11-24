package kmps;

import java.text.ParseException;

import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import kmps.header.contact.MyContactAddress;

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
		//System.out.println(req.toString());
        if (e.getRequest().getMethod().equals(Request.REGISTER)) {
        	//System.out.println("REQUEST");
        	if (ctr.reqList.existsByLocation(e, false)){
        		System.out.println("<-- Request Exists");
        		Req tmp = ctr.reqList.getByLocation(e);
        		//Ak sa relogujem
        		if(tmp.value == Status.ACK){
        			ctr.disconnected(tmp);
        			MyContactAddress cParser = MyContactAddress.getContactAddressByEvent(e);
        			if (ctr.getAccount(cParser.getName()) != null){
	        			tmp.init(ctr.getAccount(cParser.getName()));
        			}
        			//ZLA EXTENSION
        			else{
        				try {
            				System.out.println("<-- ERR: Wrong Extention " + cParser.getName());
    						ctr.respond(e, ctr.messageFactory.createResponse(Response.BAD_EXTENSION, e.getRequest()));
    					} catch (ParseException e1) {
    						e1.printStackTrace();
    					}
        			}
        		}
        		tmp.changeState(ctr, e);
        	}
        	else{
        		MyContactAddress cParser = MyContactAddress.getContactAddressByEvent(e);
        		if (ctr.getAccount(cParser.getName()) != null){
        			System.out.println("<-- Creating Request");
	        		Req tmp = new Req(e, ctr.getAccount(cParser.getName()));
	        		ctr.reqList.add(tmp);
	        		tmp.changeState(ctr, e);
        		}
        		//ZLA EXTENSION
        		else{
        			try {
        				System.out.println("<-- ERR: Wrong Extention " + cParser.getName());
						ctr.respond(e, ctr.messageFactory.createResponse(Response.BAD_EXTENSION, e.getRequest()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
        		}
        	}
        }
        else if (e.getRequest().getMethod().equals(Request.INVITE)) {
        	System.out.println("INVITE");
        	MyContactAddress cParser = MyContactAddress.getContactAddressByEvent(e);
        	if (ctr.reqList.existsByAccount(ctr.getAccount(cParser.getName()), true)){
        		CallIdHeader id = (CallIdHeader) e.getRequest().getHeader(CallIdHeader.NAME);
				if (ctr.conList.exists(id)){
					System.out.println("SAME ID");
					Connection con = ctr.conList.getById(id);
					con.changeState(ctr, e);
				}
				else{
					System.out.println("DIF ID");
					SipURI tmp = (SipURI)((FromHeader)e.getRequest().getHeader("from")).getAddress().getURI();
					String caller = tmp.getUser();
					tmp = (SipURI)((ToHeader)e.getRequest().getHeader("to")).getAddress().getURI();
					String callee = tmp.getUser();
					Connection con = new Connection(ctr.reqList.getByExt(caller), ctr.reqList.getByExt(callee), id);
					ctr.conList.add(con);
					con.changeState(ctr, e);
				}
        	}
        	//user is not registered
        	else{
        		
        	}
        }
        else if (e.getRequest().getMethod().equals(Request.ACK)) {
        	CallIdHeader id = (CallIdHeader)e.getRequest().getHeader(CallIdHeader.NAME);
            if (ctr.conList.exists(id)) {
                Connection con = ctr.conList.getById(id);
                con.changeState(ctr, e);
            }
        }
	}

	@Override
	public void processResponse(ResponseEvent e) {
		System.out.println("RESPONSE");
		CallIdHeader id = (CallIdHeader)e.getResponse().getHeader(CallIdHeader.NAME);
    	if (ctr.conList.exists(id)){
    		Connection con = ctr.conList.getById(id);
			con.changeState(ctr, e);
    	}
    	//No comunication found
    	else{
    		
    	}
		
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
