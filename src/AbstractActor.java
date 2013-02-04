import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public abstract class AbstractActor extends UntypedActor{
	
	protected ActorRef terminal;
	
	protected int terminalSpacing;
	
	public AbstractActor( int termSpaces, ActorRef term){
		terminalSpacing = termSpaces;
		terminal = term;
	}
	
	public void printToTerminal( String content){
		Message message = new Message( content, terminalSpacing );
		terminal.tell( message );
	}

	public int getSpacingSize(){
		return terminalSpacing;
	}
}
