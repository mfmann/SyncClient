                              import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.kohsuke.args4j.*;

import filesync.SynchronisedFile;


public class syncclient {
	
	public static void main(String[] args){
			
        ClientCommandLine line = parseCommandLine(args);
    
		String fileName = line.filename();
		String hostName = line.hostname();
		int serverport = line.serverport();		
		int blocksize = line.blocksize();	
		String direction = line.direction();
		
		startSync(fileName, hostName, serverport, blocksize, direction);		
	}

	private static void startSync(String fileName, String hostName, int serverport, int blocksize, String direction) {
		SynchronisedFile file = createFile(fileName, blocksize);
		
		//Creates a socket 
		DatagramSocket socket = createDatagramSocket(); 
		
		//Sends a negotiation message to the server
		NegotiationMessage negotiation = new NegotiationMessage(blocksize, direction);
		InetAddress serverAddress = getServerAddress(hostName);
		sendPacket(serverport, socket, negotiation, serverAddress);
		SyncInstructions instructions = new SyncInstructions(socket, file, blocksize, serverport, serverAddress);
		determineDirectionAndSync(direction, instructions);
	}

	public static SynchronisedFile createFile(String fileName, int blocksize) {
		SynchronisedFile file = null;
		try{
			file = new SynchronisedFile(fileName, blocksize);		
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		return file;
	}

	public static void determineDirectionAndSync(String direction, SyncInstructions instructions) {
		if(direction.equals("push")){
			instructions.SyncAsClient();
		}else if(direction.equals("pull")){
			instructions.SyncAsServer();
		}
	}

	private static void sendPacket(int serverport, DatagramSocket socket, NegotiationMessage negotiation,
			InetAddress serverAddress) {
		byte[] sendBuffer = negotiation.ToJSON().getBytes();
		DatagramPacket toSend = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverport);
		
		 try {
			socket.send(toSend);
		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static InetAddress getServerAddress(String hostName) {
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(hostName);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		return serverAddress;
	}

	public static DatagramSocket createDatagramSocket() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return socket;
	}

	public static ClientCommandLine parseCommandLine(String[] args) {
		ClientCommandLine line = new ClientCommandLine();
        CmdLineParser parser = new CmdLineParser(line);
        try {
        	parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(-1);
        }
		return line;
	}
}
