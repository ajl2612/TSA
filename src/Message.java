/**
 * Container class for String messages and its associated indentation.  
 * @author Andrew Lyne
 *
 */
public class Message {

	/**
	 * String containing the message.
	 */
	private final String contents;
	
	/**
	 * Int representing the number of indent spaces needed before printing 
	 * this message.
	 */
	private final int indentSize;
	
	/**
	 * Constructor for Message
	 * 
	 * @param contents - String containig the message
	 * @param indentSize - number of spaces to place before message
	 */
	public Message( String contents, int indentSize){
		this.contents = contents;
		this.indentSize = indentSize;
	}
	
	/**
	 * Accessor for the String message 
	 * 
	 * @return - message
	 */
	public String getContents(){
		return contents;
	}
	
	/**
	 * Accessor for the number of spaces needed. 
	 * 
	 * @return - number of spaces. 
	 */
	public int getIndentSize(){
		return indentSize;
	}
}
