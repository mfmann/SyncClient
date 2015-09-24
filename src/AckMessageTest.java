import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Assert;

public class AckMessageTest {

	@Test(expected=Exception.class)
	public void ifAckMessageIsNull() {
		AckMessage msg = new AckMessage();
		msg.FromJSON(null);
	}
	
	@Test
	public void ifAckMessageIsNotNull() {
		long counter = 10;
		AckMessage msg = new AckMessage(counter);
		String jsonMsg = msg.ToJSON();
		msg.FromJSON(jsonMsg);
		assertSame(msg.counter, counter);
	}

}
