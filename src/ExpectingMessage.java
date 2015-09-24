import org.json.simple.JSONObject;

//Message to be sent after server has received a message out of order
//"counter" specified the number of the messaged that is expected
public class ExpectingMessage extends CounterMessage{
		
	public ExpectingMessage(){
		this.type = "expecting";
	}
	
	public ExpectingMessage(long counter){
		this.type = "expecting";
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