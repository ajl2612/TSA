import akka.actor.UntypedActor;

/**
 * Actor with the sole purpose of receiving messages, formatting them and 
 * printing them to a log. 
 * 
 * @author Andrew Lyne
 */
public class Terminal extends UntypedActor{
	
	public void onReceive( Object message ){
		if( message instanceof Message){
			
			Message content = (Message) message;
			String toPrint = "";
			for(int i=0; i < content.getIndentSize(); i++){
				toPrint = toPrint.concat(" ");
			}
			toPrint = toPrint.concat(content.getContents());
			System.out.println( toPrint );
		}else
		
		if(message instanceof EndDay ){
			System.out.println("Terminal recieved end of say message.");
			getContext().stop();	
		}
		else{
			System.out.println("Terminal recieved invalid message: " +
					message.toString());
		}
	}
		
		/**
		 * Override of default stop function in actor. Prints a message to 
		 * Terminal actor before shutting down.  
		 */
		@Override
	public void postStop() {
			System.out.println( "Print terminal shutting down" );
	}	
}


