import org.json.simple.JSONObject;
import filesync.Instruction;

public class InstMessage extends CounterMessage{
	
	Instruction instruction;
	
	public InstMessage(){
		this.type = "isnt";
	}
	
	public InstMessage(Instruction instruction, long counter){
		this.type = "inst";
		this.instruction = instruction;
		this.counter = counter; 
	}
	
	public Instruction instruction(){
		return instruction;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON(){			
		String stringInstruction = instruction.ToJSON();
		
		JSONObject obj = new JSONObject();
		obj.put("type", type);
		obj.put("inst", stringInstruction);
		obj.put("counter", counter);	
		return obj.toJSONString();
	}
		
	@Override
	public void FromJSON(String message) {
		JSONObject obj=null;	
		try {
			obj = (JSONObject) parser.parse(message);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if(obj!=null){
			String stringInstruction = (String) obj.get("inst");
			instruction = factory.FromJSON(stringInstruction);
			counter = (Long) obj.get("counter");
		}		
	}
}

