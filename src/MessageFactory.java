import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageFactory {
	
	public MessageFactory(){	
	}
	
	//Returns a message object from a JSON string
	public Message FromJSON(String string){
		string = string.trim();
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject() ;
		try {
			obj = (JSONObject) parser.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			e.getUnexpectedObject();
			return null;
		}
		if(obj!=null){
			Message message=null;
			if(obj.get("type").equals("negotiation")){
				message = new NegotiationMessage();
			}else if(obj.get("type").equals("inst")){
				message = new InstMessage();
			}else if(obj.get("type").equals("ack")){
				message = new AckMessage();
			}else if(obj.get("type").equals("expecting")){
				message = new ExpectingMessage();
			}else if(obj.get("type").equals("exception")){
				message = new ExceptionMessage();
			}
			message.FromJSON(string);
			return message;
		} else return null;
	}
}
