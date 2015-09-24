import org.kohsuke.args4j.*;

public class ClientCommandLine {
	
	@Option(name = "-file", required = true)
	private String filename;
	
	@Option(name = "-hostname", required = true)
	private String hostname;
	
	@Option(name = "-p", required = false, usage = "serverport")
	private int serverport = 4144;
	
	@Option(name = "-b", required = false, usage = "blocksize")
	private int blocksize = 1024;

	@Option(name = "-d", required = false, usage = "direction")
	private String direction = "push";
	
	public String filename(){
		return filename;
	}
	
	public String hostname(){
		return hostname;
	}
	
	public int serverport(){
		return serverport;
	}
	
	public int blocksize(){
		return blocksize;
	}
	
	public String direction(){
		return direction;
	}
	
}
