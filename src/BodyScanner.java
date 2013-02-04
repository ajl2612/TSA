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
public class BodyScanner extends AbstractActor {
	
	private int CHECK_TIME = 2000;
	private final int PERCENT_FAIL = 20;
	
	private final int stationNumber;
	private ActorRef security;
	private Random r = new Random();
	
	public BodyScanner(int stationNumber, ActorRef security, ActorRef terminal){
		super(ActorFactory.QUEUE_SPACE, terminal);
		this.security = security;
		this.stationNumber = stationNumber;
	}
	
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
	}
	
	public boolean checkPerson() throws InterruptedException{
		// Scanning a person takes 2 seconds 
		Thread.sleep(CHECK_TIME);
		return (r.nextInt(100) < PERCENT_FAIL);
	}
	
	public int getStationNumber(){
		return stationNumber;
	}
}
