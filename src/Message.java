import org.json.simple.parser.JSONParser;

import filesync.InstructionFactory;

abstract public class Message {
	
	String type;
	
	protected static final JSONParser parser = new JSONParser();
	
	protected static final InstructionFactory factory = new InstructionFactory();
	
	abstract public String ToJSON();
	
	abstract public void FromJSON(String jst);
	
	public String type(){
		return type;
	}
}
