import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AckMessageTest.class, ClientCommandLineTest.class, ClientThreadTest.class, ExpectingMessageTest.class,
		NegotiationMessageTest.class, syncclientTest.class })
public class AllTests {

}
