
public class Baggage {
	
	private final int numBags;
	private final Person owner;
	
	public Baggage(int numBags, Person p){
		this.numBags = numBags;
		owner = p;
	}
	
	public int getNumBags(){
		return numBags;
	}
	
	public Person getOwner(){
		return owner;
	}
}
