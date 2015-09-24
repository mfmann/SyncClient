import static org.junit.Assert.*;

import org.junit.Test;

public class NegotiationMessageTest {

	@Test(expected=Exception.class)
	public void ifNegotiationMessageIsNull() {
		NegotiationMessage msg = new NegotiationMessage();
		msg.FromJSON(null);
	}
	
	@Test
	public void ifNegotiationMessageIsNotNull() {
		
		long blocksize = 1024;
		String direction = "pull";
		NegotiationMessage msg = new NegotiationMessage(blocksize, direction);
		
		String jsonMsg = msg.ToJSON();
		msg.FromJSON(jsonMsg);
		
		assertEquals(msg.blocksize, blocksize);
		assertEquals(msg.direction, direction);
	}
	
	@Test
	public void testIfReturnCorrectBlocksize(){
		
		long blocksize = 1024;
		String direction = "pull";
		NegotiationMessage msg = new NegotiationMessage(blocksize, direction);
		
		assertEquals(msg.blocksize(), blocksize);
		
	}
	
	@Test
	public void testIfReturnCorrectDirection(){
		
		long blocksize = 1024;
		String direction = "pull";
		NegotiationMessage msg = new NegotiationMessage(blocksize, direction);
		
		assertEquals(msg.direction(), direction);
		
	}


}
