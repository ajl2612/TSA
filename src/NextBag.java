
public class NextBag {
	private final Baggage next;
		
	public NextBag( Baggage b){
		next = b;
	}
		
	public Baggage getNext(){
		return next;
	}

}
