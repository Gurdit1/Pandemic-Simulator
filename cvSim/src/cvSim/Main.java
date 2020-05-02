package cvSim;

import java.util.*;
import java.io.*;

public class Main {
	
	private static double mortalityRate = 2.3; //Percentage
	private static int minDaysInfected = 10;
	private static boolean selfIsolation = true;
	private static boolean immunityAfterInfected = true;
	
	
	public static void main(String args[]) {
		int iter=110,
				original, infected, recovered, dead;
		
		int n = 1000;
		Person[][] pop = new Person[n][n];
		pop = originalPop(pop, n);
		
		pop = randomStart(pop, n); //First person
		
		System.out.println("Iteration, Uninfected, Infected, Recovered, Dead");
		for(int gen=1; gen<=iter;++gen) {
			//Counters
			original = counter(0, pop, n);
			infected = counter(1, pop, n);
			recovered = counter(2, pop, n);
			dead = counter(3, pop, n);
			original += counter(4, pop, n);
			
			//Display data
			System.out.println(String.valueOf(gen) + "," +
			          String.valueOf(original) + "," +
					  String.valueOf(infected) + "," +
					  String.valueOf(recovered) + "," +
					  String.valueOf(dead));
			
			//Infect others
			for(int i=0; i<n; ++i){
				for(int j=0; j<n; ++j) {
					pop = infectAdjacent(pop, gen, i, j, n);
				}
			}
			
			//Recovery/Death check
			pop = recoverOrDie(pop, n, gen);
			
			if(selfIsolation==true) {
				pop = isolation(pop, n, infected);
			}
			
			//Shuffle population
			pop = shufflePop(pop, n);
			
		}		
	}
	
	public static Person[][] isolation(Person[][] pop, int n, int infected){
		//Person more likely to self-isolate the higher the current number of infected people
		
		int ranNum, popSize = n*n;
		int otherParameter;
		
		for(int i=0; i<n; ++i) {
			for(int j=0; j<n;++j) {
				
				
				if(immunityAfterInfected==true) {
					otherParameter = 4;
				}
				else {
					otherParameter = 3;
				}
				
				//Checks if individual is not infected
				if(((pop[i][j].getState()==0)||(pop[i][j].getState()==4))||(pop[i][j].getState()==otherParameter)){
					ranNum = UI(0, popSize);
					
					if(ranNum<infected) {
						pop[i][j].setState(4);
					}
					else {
						pop[i][j].setState(0);
					}
				}
				
			}
		}
		
		return pop;
	}
	
	public static int counter(int ref, Person[][] pop, int n){
		int s = 0;
		
		for(int i=0; i<n; ++i) {
			for(int j=0; j<n; ++j) {
				if(pop[i][j].getState()==ref) {
					s+=1;
				}
			}
		}
		
		return s;
	}
	
	public static void displayArrayList(ArrayList<Integer> arr){
		for(int i=0; i<arr.size();++i) {
			System.out.println(arr.get(i));
		}
	}
	
	public static Person[][] randomStart(Person[][] pop, int n){
		//Random starting infection
		int i = UI(0, n -1);
		int j = UI(0, n -1);
		pop[i][j].setState(1);
		pop[i][j].setTimeAtInfection(0);
		return pop;
	}
	
	public static Person[][] recoverOrDie(Person[][] pop, int n, int time){
		//Takes entire population and checks if they meet the requirement to move on from the infection
		
		for(int i=0; i<n; ++i) {
			for(int j=0; j<n; ++j) {
				
				//Checks if infected
				if(pop[i][j].getState()==1) {
					int x = UI(0,1000); //Probability of dying if no longer infected 
					int y = UI(0,100); //Probability of remaining infected on given day
					//Checks if sufficient number of days has passed
					
					if(time-pop[i][j].getTimeAtInfection()>minDaysInfected) {
						if(y>50) {
							//If x<(mortalityRate*10) then person dies else they recover
							if(x<(mortalityRate*10)) {
								pop[i][j].setState(3);
							}
							else {
								pop[i][j].setState(2);
							}
						}
					}
					
				}
			}
		}
		
		return pop;
	}
	
	public static Person[][] infectAdjacent(Person[][] pop, int time, int i, int j, int n){
		//Infects adjacent people if their state equals 0
		int otherParameter;
		if(immunityAfterInfected==true) {
			otherParameter = 1;
		}
		else {
			otherParameter = 4;
		}
		
		//Checks if person is infected
		if((pop[i][j].getState()==1)||(pop[i][j].getState()==otherParameter)) {
			
			//System.out.println("After infect check");
			
			//Infects above
			if(i!=0) {//Checks if not at edge
				if(pop[i-1][j].getState()==0) {
					pop[i-1][j].setState(1);
					pop[i-1][j].setTimeAtInfection(time);	
				}
			}
			
			//Infects diagonal up and left
			else if((i!=0)&&(j!=0)) {
				if(pop[i-1][j-1].getState()==0) {
					pop[i-1][j-1].setState(1);
					pop[i-1][j-1].setTimeAtInfection(time);	
				}
			}
			
			//Infects left
			else if(j!=0) {//Checks if not at edge
				if(pop[i][j-1].getState()==0) {
					pop[i][j-1].setState(1);
					pop[i][j-1].setTimeAtInfection(time);	
				}
			}
			
			//Infects diagonal left and down
			else if((i!=(n-1))&&(j!=0)) {
				if(pop[i+1][j-1].getState()==0) {
					pop[i+1][j-1].setState(1);
					pop[i+1][j-1].setTimeAtInfection(time);	
				}
			}
			
			//Infects below
			else if(i!=(n-1)) {//Checks if not at edge
				if(pop[i+1][j].getState()==0) {
					pop[i+1][j].setState(1);
					pop[i+1][j].setTimeAtInfection(time);	
				}
			}
			
			//Infects diagonal down and right
			else if((i!=(n-1))&&(j!=(n-1))) {
				if(pop[i+1][j+1].getState()==0) {
					pop[i+1][j+1].setState(1);
					pop[i+1][j+1].setTimeAtInfection(time);	
				}
			}
			
			//Infects right
			else if(j!=(n-1)) {//Checks if not at edge
				if(pop[i][j+1].getState()==0) {
					pop[i][j+1].setState(1);
					pop[i][j+1].setTimeAtInfection(time);	
				}
			}
			
			//Infects diagonal up and right
			else if((i!=0)&&(j!=(n-1))) {
				if(pop[i-1][j+1].getState()==0) {
					pop[i-1][j+1].setState(1);
					pop[i-1][j+1].setTimeAtInfection(time);	
				}
			}
			
		}
		
		return pop;
	}
	
	public static Person[][] originalPop(Person[][] pop, int n) {
		//Creates population of state 0
		for(int i=0; i<n;++i) {
			for(int j=0; j<n;++j) {
				pop[i][j] = new Person(0);
			}
		}
		return pop;
	}
	
	public static ArrayList<Integer> randomUniqueNumbers(int n){
		ArrayList<Integer> ranArr = new ArrayList<Integer>();
		
		for(int i=0; i<n; ++i) {
			int x = UI(0, n-1);
			while(isContain(ranArr, x)==true) {
				x = UI(0, n-1);
			}
			ranArr.add(x);
		}
		
		return ranArr;
	}
	
	public static boolean isContain(ArrayList<Integer> arr, int n) {
		//Checks if a number is in an array
		boolean contains = false;
		
		int i=0;
		while((i<arr.size())&&(contains==false)) {
			if(arr.get(i)==n) {
				contains = true;
			}
			else {
				i+=1;
			}
		}
		return contains;		
	}
	
	public static Person[][] shufflePop(Person[][] pop, int n) {
		Person[][] newPop = new Person[n][n];
		
		ArrayList<Integer> arrI = randomUniqueNumbers(n);
		ArrayList<Integer> arrJ = randomUniqueNumbers(n);
		
		for(int i=0; i<n;++i){
			for(int j=0; j<n; ++j){
				newPop[i][j] = pop[arrI.get(i)][arrJ.get(j)];
			}
		}
		
		return newPop;
	}
	
	
	static public int UI(int aa,int bb)
	{
		Random rand = new Random();
		int a = Math.min(aa,bb);
		int b = Math.max(aa,bb);
		//if (rand == null) 
		//{
		//	rand = new Random();
		//	rand.setSeed(System.nanoTime());
		//}
		int d = b - a + 1;
		int x = rand.nextInt(d) + a;
		return(x);
	}

}
