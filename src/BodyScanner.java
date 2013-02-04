import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Stub
 * 
 * @author Chris
 * @author Carol
 * @version The Andy Lyne Version
 *
 */
public class BodyScanner extends UntypedActor {
	
	private int CHECK_TIME = 2000;
	private ActorRef security;
	private Random r = new Random();
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof Person){
			BodyScanResults results;
			boolean didPass = false;
			try{
				didPass = checkPerson();
			}catch( InterruptedException e){
				System.err.println("Body Inspection Interrupted: Automatic Fail");
				didPass = false;
			}finally{
				results = new BodyScanResults((Person)message, didPass);
				security.tell(results);
			}
		}
		else if (message instanceof ScanConfigure){
			security = ((ScanConfigure)message).getSecurity();
		}
	}
	
	public boolean checkPerson() throws InterruptedException{
		// Scanning a person takes 2 seconds 
		Thread.sleep(CHECK_TIME);
		return (r.nextInt(5) == 0);
	}
}
