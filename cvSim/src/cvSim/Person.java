package cvSim;

public class Person {
	
	
	private int state; // 0 = Uninfected, 1 = Infected, 2 = Recovered, 3 = Dead, 4 = Self-Isolated (Uninfected)
	private int timeAtInfection; //set timeAtInfection at when state = 1
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getTimeAtInfection() {
		return timeAtInfection;
	}
	public void setTimeAtInfection(int timeAtInfection) {
		this.timeAtInfection = timeAtInfection;
	}
	
	public Person(int state) {
		this.state = state;
	}
	

}
