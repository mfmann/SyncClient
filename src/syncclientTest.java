import static org.junit.Assert.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import filesync.Instruction;

public class syncclientTest {

	
	
	String[] args;
	
	@Test(expected=Exception.class)
	public void testCreateFile() {
		String name = null;
		int blocksize = 1024;
		syncclient.createFile(name, blocksize);
	}
	
	@Test
	public void testDatagramSocket() {
		
	}
	
	@Test
	public void testDetermineDirectionAndSync(){
		SyncInstructions inst = Mockito.mock(SyncInstructions.class);
		syncclient.determineDirectionAndSync("pull", inst);
		Mockito.verify(inst).SyncAsServer();
	}
	
	@Test
	public void testDetermineDirectionAndSync2(){
		SyncInstructions inst = Mockito.mock(SyncInstructions.class);
		syncclient.determineDirectionAndSync("push", inst);
		Mockito.verify(inst).SyncAsClient();
	}
	
	
	@Test
	public void testCreateDatagram(){
		DatagramSocket socket = syncclient.createDatagramSocket();
		assertNotNull(socket);
	}
	
	
}

