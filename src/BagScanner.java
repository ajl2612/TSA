import java.util.Random;
import akka.actor.ActorRef;


/**
 * Stub
 * 
 * @author Chris
 * @author Carol
 * @category Andy "The Guy" Lyne
 *
 */
public class BagScanner extends AbstractActor {
	
	private final int CHECK_TIME = 2000;
	private final int PERCENT_FAIL = 20;
	
	private final int stationNumber;
	private ActorRef security;
	private Random r = new Random();
	
	
	public BagScanner(int stationNumber, ActorRef security, ActorRef terminal){
		super(ActorFactory.SCAN_SPACE, terminal);
		this.security = security;
		this.stationNumber = stationNumber;
	}
	
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
		}else if( message instanceof EndDay){
			security.tell((EndDay)message);
			getContext().stop();
		}else{
			System.err.println("BagScan recieved invalid message: " + 
					message.toString());
		}
	}
	
	@Override
	public void postStop() {
		printToTerminal( "Baggage Scanner " + stationNumber + "Closed" );
	}
	
	public boolean checkBags(Baggage bags) throws InterruptedException{
		for( int i=0; i< bags.getNumBags(); i++){
			Thread.sleep(CHECK_TIME);
			if(r.nextInt(100) < PERCENT_FAIL)
				return false;
		}
		return true;
	}
	
	public int getStationNumber(){
		return stationNumber;
	}
}