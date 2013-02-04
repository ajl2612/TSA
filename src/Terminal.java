import akka.actor.UntypedActor;

public class Terminal extends UntypedActor{
	
	public void onReceive( Object message ){
		if( message instanceof Message){
			Message content = (Message) message;
			String toPrint = "";
			for(int i=0; i < content.getIndentSize(); i++){
				toPrint.concat(" ");
			}
			toPrint.concat(content.getContents());
			System.out.println( toPrint );
		}else{
			System.out.println("Terminal recieved invalid message: " +
					message.toString());
		}
	}

}
