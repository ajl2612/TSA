import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Stub
 * 
 * @author Chris
 * @author Carol
 * @category Andy "The Guy" Lyne
 *
 */
public class BagScanner extends UntypedActor {
	
	private int CHECK_TIME = 2000;
	private ActorRef security;
	private Random r = new Random();
	
	public void onReceive(Object message){
		if (message instanceof Baggage){
			BagScanResults results;
			boolean didPass = false;
			try{
				didPass = checkBags((Baggage)message);
			}catch( InterruptedException e){
				System.err.println("Bag Inspection Interrupted: Automatic Fail");
				didPass = false;
			}finally{
				results = new BagScanResults((Baggage)message, didPass);
				security.tell(results);
			}
		}
		else if (message instanceof ScanConfigure){
			security = ((ScanConfigure)message).getSecurity();
		}
	}
	
	public boolean checkBags(Baggage bags) throws InterruptedException{
		boolean didPass = true;
		for( int i=0; i< bags.getNumBags(); i++){
			Thread.sleep(CHECK_TIME);
			if(r.nextInt(5) == 0)
				didPass = false;
		}
		return didPass;
	}
}
