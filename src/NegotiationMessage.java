import org.json.simple.JSONObject;


public class NegotiationMessage extends Message {
	
	long blocksize;
	String direction;
	
	public NegotiationMessage(){
		this.type = "negotiation";
	}
	
	public NegotiationMessage(long blocksize, String direction){
		this.type = "negotiation";
		this.blocksize = blocksize;
		this.direction = direction;
	}
	
	public long blocksize(){
		return blocksize;
	}
	
	public String direction(){
		return direction;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {		
		JSONObject obj = new JSONObject();
		obj.put("type", type);
		obj.put("blocksize", blocksize);
		obj.put("direction", direction);	
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
			blocksize = (Long) obj.get("blocksize");
			direction = (String) obj.get("direction");
		}		
	}	
}

