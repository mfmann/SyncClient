import org.kohsuke.args4j.Option;

public class ServerCommandLine {
	
	@Option(name = "-file", required = true)
	private String filename;
	
	@Option(name = "-p", required = false, usage = "serverport")
	private int serverport = 4144;
	
	public String filename(){
		return filename;
	}
	
	public int serverport(){
		return serverport;
	}
}