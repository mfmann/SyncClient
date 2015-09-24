 import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import filesync.CopyBlockInstruction;
import filesync.Instruction;
import filesync.NewBlockInstruction;
import filesync.SynchronisedFile;


public class ClientThread implements Runnable{
	
	SynchronisedFile file;
	DatagramSocket socket;
	long clientCounter;
	InetAddress remoteAddress;
	int remotePort;
	
	ClientThread(SynchronisedFile file, DatagramSocket socket, InetAddress remoteAddress, int remotePort){
		this.file=file;
		this.socket=socket;
		this.clientCounter = 1;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
	}
	
	
	public void run(){
		
		Instruction nextInstruction;
		Instruction currentInstruction;
		Boolean sent;
		MessageFactory factory = new MessageFactory();

		while((nextInstruction=file.NextInstruction())!= null){
			
			sent = false;
			//Resets counter to 1 if receives a "StartUpdate" instruction
			determineClientCounter(nextInstruction);	
			currentInstruction=nextInstruction;
		
			//Attempts to send a message containing instructions until an "ack" message is received
			while(sent==false){
			
				 DatagramPacket toSend = createDatagramPacketToSend(currentInstruction);
				 //Sets socket to timeout after 10 seconds
				setSocketToTimeout(10000);	
				CounterMessage msgFromServer = sendDataAndGetReply(factory, toSend);
				sent = determineIfMessageSent(msgFromServer);	
				if(sent == false){
					currentInstruction=new NewBlockInstruction((CopyBlockInstruction)currentInstruction);
				}
			 }
		}
	}

	public void determineClientCounter(Instruction nextInstruction) {
		if(nextInstruction.Type().equals("StartUpdate")){
			clientCounter = 1;
		}
	}

	public DatagramPacket createDatagramPacketToSend(Instruction currentInstruction) {
		InstMessage toServer = new InstMessage(currentInstruction, clientCounter);	 
		byte[] sendBuffer = toServer.ToJSON().getBytes();
		DatagramPacket toSend = new DatagramPacket(sendBuffer, sendBuffer.length, remoteAddress, remotePort);
		return toSend;
	}

	private CounterMessage sendDataAndGetReply(MessageFactory factory, DatagramPacket toSend) {
		byte[] replyBuffer = new byte[1024];	 
		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length);
		sendPacketAndReceiveReply(toSend, reply);
		CounterMessage msgFromServer = readReplyFromServer(factory, reply);
		return msgFromServer;
	}

	private CounterMessage readReplyFromServer(MessageFactory factory, DatagramPacket reply) {
		String stringReply = new String (reply.getData());
		System.out.println(stringReply);
		CounterMessage msgFromServer = (CounterMessage) factory.FromJSON(stringReply);
		return msgFromServer;
	}

	public boolean determineIfMessageSent(CounterMessage msgFromServer) {
		Boolean sent = false;
		
		String type = msgFromServer.type();
		long serverCounter = msgFromServer.counter();
		
		//Next instruction is sent in a message if an "ack" is received
		 if(type.equals("ack") && clientCounter==serverCounter){
			 sent = true;
			 clientCounter++;	
		//Next instruction is sent if the server already received this message
		 }else if(type.equals("expecting") && serverCounter==(clientCounter+1)){
			 sent = true;
		//Instruction is upgraded from a "NewBlock" to a "CopyBlock" and resent
		 }else if(type.equals("exception")){
			 sent = false;
		 }
		 return sent;
	}

	public void sendPacketAndReceiveReply(DatagramPacket toSend, DatagramPacket reply) {
		boolean received = false;
		//Message will be resent if client does not receive a reply from the server in 10 seconds
		while(received == false){			
			sendPacket(toSend); 
			received = receiveFromSocket(reply);
		}
	}

	public boolean receiveFromSocket(DatagramPacket reply) {
		boolean received = false;
		try {
			socket.receive(reply);
			received = true;
		//If socket timesout, message has to be resent	
		}catch (SocketTimeoutException e){
			received = false;			
		}catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
		}
		return received;
	}

	public void sendPacket(DatagramPacket toSend) {
		try {
			socket.send(toSend);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void setSocketToTimeout(int time) {
		try {
			socket.setSoTimeout(time);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}
}
