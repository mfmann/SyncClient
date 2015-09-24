import static org.junit.Assert.*;

import org.junit.Test;

public class ExpectingMessageTest {

	@Test(expected=Exception.class)
	public void ifExpectingMessageIsNull() {
		ExpectingMessage msg = new ExpectingMessage();
		msg.FromJSON(null);
	}
	
	@Test
	public void ifExpectingMessageIsNotNull() {
		long counter = 10;
		ExpectingMessage msg = new ExpectingMessage(counter);
		String jsonMsg = msg.ToJSON();
		msg.FromJSON(jsonMsg);
		assertSame(msg.counter, counter);
	}

}
