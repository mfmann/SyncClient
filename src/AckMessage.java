import org.json.simple.JSONObject;

//Message to be sent after server has processed instructions without exception
//"counter" specifies the number of the message that was processed successfully
public class AckMessage extends CounterMessage{
		
	public AckMessage(){
		this.type = "ack";
	}
	
	public AckMessage(long counter){
		this.type = "ack";
		this.counter = counter; 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON(){			
		
		JSONObject obj = new JSONObject();
		obj.put("type", type);
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
			counter = (Long) obj.get("counter");
		}		
	}
}
	
	


