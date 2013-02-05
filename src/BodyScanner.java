import java.util.Random;
import akka.actor.ActorRef;

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
		super(ActorFactory.SCAN_SPACE, terminal);
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
		}else if( message instanceof EndDay){
			security.tell((EndDay)message);
			getContext().stop();
		}else{
			System.err.println("BodyScan recieved invalid message: " + 
					message.toString());
		}
	}
	
	@Override
	public void postStop() {
		printToTerminal( "Body Scanner " + stationNumber + "Closed" );
	}
	
	public boolean checkPerson() throws InterruptedException{
		// Scanning a person takes 2 seconds 
		Thread.sleep(CHECK_TIME);
		return (r.nextInt(100) < PERCENT_FAIL);
	}
	
	/**
	 * Accessor for which security station this scanner belongs to. 
	 * @return - station number
	 */
	public int getStationNumber(){
		return stationNumber;
	}
}
