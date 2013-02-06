import java.util.Random;

import akka.actor.ActorRef;

/**
 * Main class for TSA system
 *
 * @author Andrew
 */
public class Driver {

	private final static int NUM_SECURITY_LINES = 5;
	
	private final static int NUM_PEOPLE = 100;
	
	private final static int MAX_NUM_BAGS = 5;
	
	public static void main(String[] args){		
		
		ActorRef terminal = ActorFactory.makeTerminal();
		
		ActorRef jail = ActorFactory.makeJail(terminal, NUM_SECURITY_LINES);
		
		ActorRef[] queues = new ActorRef[NUM_SECURITY_LINES];
		for(int i=1; i<=NUM_SECURITY_LINES; i++){
			queues[i-1] = ActorFactory.makeSecurityStation(terminal, i, jail);
		}
		ActorRef docCheck = ActorFactory.makeDocumentChecker(terminal, queues);
		docCheck.start();
		
		Random r = new Random();
		for( int i = 1; i <= NUM_PEOPLE; i++){
			docCheck.tell(new Person(i,r.nextInt(MAX_NUM_BAGS+1)));
		}
		
		docCheck.tell( new EndDay());
		
		
	}
	
}
