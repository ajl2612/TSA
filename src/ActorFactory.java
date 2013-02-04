import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.UntypedActorFactory;
import static akka.actor.Actors.actorOf;

public class ActorFactory {

	public static final int DOC_CHECK_SPACE = 0;
	public static final int QUEUE_SPACE = 1;
	public static final int SCAN_SPACE = 2;
	public static final int SECURITY_SPACE = 3;
	public static final int JAIL_SPACE = 4;
	
	public static ActorRef makeTerminal(){
		ActorRef term = actorOf( Terminal.class );
		term.start();
		return term;
	}
	
	public static ActorRef makeSecurityStation(final ActorRef terminal, final int lineNumber, final ActorRef jail){
		
		final ActorRef newSecurity = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new Security( lineNumber, jail, terminal);
			}
		});
		
		final ActorRef newBagScan = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new BagScanner( lineNumber, newSecurity, terminal);
			}
		});
		
		final ActorRef newBodyScan = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new BodyScanner( lineNumber, newSecurity, terminal);
			}
		});
		
		final ActorRef newQueue = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new Queue( lineNumber, newBodyScan, newBagScan, terminal);
			}
		});
		
		newSecurity.start();
		newBodyScan.start();
		newBagScan.start();
		newQueue.start();
		return newQueue;
	}
	
	public static ActorRef makeJail(final ActorRef terminal, final int numSecurityStations){
		ActorRef newActor = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new Jail(terminal, numSecurityStations);
			}
		});
		newActor.start();
		return newActor;
	}
	
	public static ActorRef makeDocumentChecker( final ActorRef terminal, final ActorRef[] queues){
		ActorRef newActor = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new DocumentChecker(terminal, queues);
			}
		});
		newActor.start();
		return newActor;
	}
	
}
