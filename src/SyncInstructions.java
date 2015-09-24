import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.File;
import java.io.IOException;

import filesync.BlockUnavailableException;
import filesync.Instruction;
import filesync.SynchronisedFile;

public class SyncInstructions {
	
	static DatagramSocket socket;
	static SynchronisedFile file;
	static long blocksize;
	static InetAddress remoteAddress;
	static int remotePort;
	
	public SyncInstructions(DatagramSocket socket, SynchronisedFile file, long blocksize, int remotePort, InetAddress remoteAddress){
		this.socket = socket;
		this.file = file;
		this.blocksize = blocksize;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
	}
	
	//Client or Server will act as the client
	public void SyncAsClient(){
		
		//Creates a thread to service the instruction queue
		Thread clientThread = new Thread(new ClientThread(file, socket, remoteAddress, remotePort));
		clientThread.start();
		
		//CheckFileState is continuously called every 5 seconds
		while(true){		
			try{
				file.CheckFileState();
			}catch(IOException e){
				e.printStackTrace();
				System.exit(-1);
			}catch(InterruptedException e){
				e.printStackTrace();
				System.exit(-1);
			}		
			try{
				Thread.sleep(50000);
			}catch(InterruptedException e){
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	//Client or Server will act as the server
	public void SyncAsServer(){
	
		Instruction instruction;
		MessageFactory factory = new MessageFactory();
		long serverCounter=1;
			
		while(true){
			
			//Receives message from client
			byte[] receiveBuffer = new byte[(int) (blocksize*2)];
			DatagramPacket received = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			try {
				socket.receive(received);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
					
			String stringMessage = new String(received.getData());
			
			//String received is converted to an "InstMessage"
			InstMessage message = (InstMessage) factory.FromJSON(stringMessage);
			instruction = message.instruction();
			
			long clientCounter = message.counter();
			
			//serverCounter is reset to 1 if a "StartUpdate" instruction is received
			if(instruction.Type().equals("StartUpdate")){
				serverCounter = 1;			
			}
			
			//Message is processed
			Message toClient = null;
			if(clientCounter == serverCounter){
				try {
					file.ProcessInstruction(instruction);
					//Server will send an "AckMessage" if instructions are processed without exception	
					toClient = new AckMessage(serverCounter);
					serverCounter++;
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				//Server will send an "ExceptionMessage" if "BlockUnavailableException" is encountered	
				}catch(BlockUnavailableException e){
					toClient = new ExceptionMessage(serverCounter);
				}
			}else{
				//Server will send an "ExpectingMessage" if it does not receive the message number it is expecting
				toClient = new ExpectingMessage(serverCounter);
			}
				
			//Server replies based on message received
			byte[] replyBuffer = toClient.ToJSON().getBytes();
			DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, received.getAddress(), received.getPort());
			try {
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}		
}


 		
