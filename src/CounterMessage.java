
//abstract class of messages that include counters

public abstract class CounterMessage extends Message {
	
	long counter;
	
	public long counter(){
		return counter;
	}
}
