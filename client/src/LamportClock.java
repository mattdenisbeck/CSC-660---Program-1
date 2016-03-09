package src;

public class LamportClock {
	private int value;
	
	public LamportClock(){
		this.setValue(0);
	}
	
	public synchronized void sleepClock(int sleepTime){
		setValue(getValue() + sleepTime);
	}
	
	public synchronized void incrementClock(int sourceClock){
		setValue(getValue() + Math.max(sourceClock + 1, getValue() + 1));
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return Integer.toString(getValue());
	}

}
