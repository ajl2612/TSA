import akka.actor.ActorRef;

/**
 * Main class for TSA system
 *
 * @author Andrew
 */
public class Driver {

	private final static int NUM_SECURITY_LINES = 3;
	
	public static void main(String[] args){		
		
		ActorRef terminal = ActorFactory.makeTerminal();
		
		ActorRef jail = ActorFactory.makeJail(terminal, NUM_SECURITY_LINES);
		
		ActorRef[] queues = new ActorRef[NUM_SECURITY_LINES];
		for(int i=0; i<NUM_SECURITY_LINES; i++){
			queues[i] = ActorFactory.makeSecurityStation(terminal, i, jail);
		}
		ActorRef docCheck = ActorFactory.makeDocumentChecker(terminal, queues);
		docCheck.start();
		
		docCheck.tell( new EndDay());
		
		
	}
	
}
