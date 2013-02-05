/**
 * Container class for data representing how many bags a given person has.
 * 
 * @author Andrew
 */

public class Baggage {
	/**
	 * Number of bags the owner of this baggage has. 
	 */
	private final int numBags;
	/**
	 * The owner of this Baggage. 
	 */
	private final Person owner;
	
	/**
	 * Constructor for Baggage.
	 * 
	 * @param numBags - number of bags to contain
	 * @param person - Person that is the owner of this baggage
	 */
	public Baggage(int numBags, Person person){
		this.numBags = numBags;
		owner = person;
	}
	
	/**
	 * Accessor for number of bags. 
	 * 
	 * @return The number of bags
	 */
	public int getNumBags(){
		return numBags;
	}
	
	/**
	 * Accessor for Person owner of bags. 
	 * 
	 * @return The Person owner of the bags
	 */
	public Person getOwner(){
		return owner;
	}
}
