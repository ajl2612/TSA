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
	
	public static ActorRef makeSecurityStation(){
		
	}
	
	public static ActorRef makeJail(ActorRef terminal, int numSecurityStations){
		ActorRef newActor = actorOf( Jail)
		
	}
	
	public static ActorRef makeDocumentChecker( ActorRef[] queues, ActorRef terminal){
		ActorRef newActor = actorOf( new UntypedActorFactory(){
			// Inline override of default constructor call to one implemented
			@Override
			public Actor create(){
				return new DocumentChecker()
			}
		});
	}
	
}
