import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import filesync.CopyBlockInstruction;
import filesync.Instruction;
import filesync.StartUpdateInstruction;
import filesync.SynchronisedFile;

public class ClientThreadTest {
	
	SynchronisedFile file = Mockito.mock(SynchronisedFile.class);
	DatagramSocket socket = Mockito.mock(DatagramSocket.class);
	long clientCounter = 1;
	InetAddress remoteAddress = Mockito.mock(InetAddress.class) ;
	int remotePort = 80;
	ClientThread ct = new ClientThread(file, socket, remoteAddress, remotePort);
	 
	 
	@Test
	public void testIfClientCounterResetWhenStartInstructionIsNext() {
	    
		ct.clientCounter = 0;
		StartUpdateInstruction start = new StartUpdateInstruction();
	    ct.determineClientCounter(start);
		assertEquals(1, ct.clientCounter);
	}
	
	@Test
	public void testIfClientCounterNotResetWhenInstructionIsNext() {
	    
		ct.clientCounter = 2;
	    CopyBlockInstruction inst = new CopyBlockInstruction();
	    ct.determineClientCounter(inst);
		assertEquals(2, ct.clientCounter);
	}
	
	@Test
	public void testIfDatagramPacketCreated() {
		
		Instruction inst = Mockito.mock(Instruction.class);
		InstMessage toServer = new InstMessage(inst, ct.clientCounter);	 
		DatagramPacket toSend =  ct.createDatagramPacketToSend(inst);
		assertNotNull(toSend);
	}
	
	@Test
	public void testSendPacket(){
		DatagramPacket packet = null;
		ct.sendPacket(packet);
	}
	
	
	@Test
	public void testDetermineIfAckMessageSent(){
		
		CounterMessage msg = new AckMessage();
		msg.counter = 1;
		ct.clientCounter = 1;
		
		boolean sent = false;
		sent = ct.determineIfMessageSent(msg);
		
		assertTrue(sent);
		assertEquals(ct.clientCounter, 2);
		
	}
	
	@Test
	public void testDetermineIfAckMessageSent2(){
		
		CounterMessage msg = new AckMessage();
		msg.counter = 2;
		ct.clientCounter = 1;
		
		boolean sent = false;
		sent = ct.determineIfMessageSent(msg);
		
		assertFalse(sent);
		assertEquals(ct.clientCounter, 1);
		
	}
	
	@Test
	public void testDetermineIfAckMessageSent3(){
		
		CounterMessage msg = new ExceptionMessage();
		msg.counter = 1;
		ct.clientCounter = 1;
		
		boolean sent = false;
		sent = ct.determineIfMessageSent(msg);
		
		assertFalse(sent);
		assertEquals(ct.clientCounter, 1);
		
	}
	
	
	@Test
	public void testDetermineIfExceptionkMessageNotSent(){
		
		CounterMessage msg = new ExceptionMessage();
		msg.counter = 1;
		ct.clientCounter = 1;
		
		boolean sent = true;
		sent = ct.determineIfMessageSent(msg);
		
		assertFalse(sent);
		assertEquals(ct.clientCounter, 1);
		
	}
	
	@Test
	public void testDetermineIfExpectingMessageIsSent(){
		
		CounterMessage msg = new ExpectingMessage();
		msg.counter = 2;
		ct.clientCounter = 1;
		
		boolean sent = false;
		sent = ct.determineIfMessageSent(msg);
		
		assertTrue(sent);
		assertEquals(ct.clientCounter, 1);
		
	}
	
	@Test
	public void testDetermineIfExpectingMessageIsSent2(){
		
		CounterMessage msg = new ExpectingMessage();
		msg.counter = 1;
		ct.clientCounter = 1;
		
		boolean sent = true;
		sent = ct.determineIfMessageSent(msg);
		
		assertFalse(sent);
		assertEquals(ct.clientCounter, 1);
		
	}
	
	@Test
	public void testTimeout(){
		ct.setSocketToTimeout(1000);
	}
	
	
	@Test 
	public void testReciveFromSocket(){
		DatagramPacket reply = null;
		boolean received = ct.receiveFromSocket(reply);
		assertTrue(received);
	}
	
	@Test
	public void testReciveFromSocket2() throws IOException{
		DatagramPacket reply = null;
		doThrow(new SocketTimeoutException()).when(ct.socket).receive(reply);
		boolean recieved = ct.receiveFromSocket(reply);
		assertFalse(recieved);
	}
	
	@Test
	public void testSendPacketAndReceiveReply() throws IOException{
		
		ClientThread c = Mockito.spy(ct);
		DatagramPacket reply = null;
		DatagramPacket toSend = null;
		c.sendPacketAndReceiveReply(toSend, reply);
		Mockito.verify(c, times(1)).sendPacket(toSend);

	}
}
	


