
public class Message {

	private final String contents;
	
	private final int indentSize;
	
	public Message( String contents, int indentSize){
		this.contents = contents;
		this.indentSize = indentSize;
	}
	
	public String getContents(){
		return contents;
	}
	
	public int getIndentSize(){
		return indentSize;
	}
}
