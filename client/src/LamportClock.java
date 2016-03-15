package src;

public class LamportClock {
	private int value; //tracks the local time for each node
	
	public LamportClock(){
		this.setValue(0);
	}
	
	//increments the clock while a node sleeps
	public void sleepClock(int sleepTime){
		setValue(getValue() + sleepTime);
	}
	
	//increments the clock when message is received
	public void incrementClock(int sourceClock){
		setValue(Math.max(sourceClock + 1, getValue() + 1));
	}

	public synchronized int getValue() {
		return value;
	}

	public synchronized void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return Integer.toString(getValue());
	}

}
