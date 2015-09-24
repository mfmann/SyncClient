import org.json.simple.JSONObject;

//Message to be sent after server has encountered a "BlockUnavailableExcpetion" 
//"counter" specifies the number of the message that was not processed successfully
public class ExceptionMessage extends CounterMessage{
		
	public ExceptionMessage(){
		this.type = "exception";
	}
	
	public ExceptionMessage(long counter){
		this.type = "exception";
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