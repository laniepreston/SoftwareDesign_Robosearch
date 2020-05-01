package ROBOSEARCH;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import java.awt.Color;
import ROBOSEARCH.Environment;
import ROBOSEARCH.EnvironmentGrid;
import ROBOSEARCH.Subject;
import ROBOSEARCH.Task;
import ROBOSEARCH.RoverFactory;
import ROBOSEARCH.Observer;
import java.util.Scanner;

public class CentralStation extends TimerTask implements Subject {

	/**
	 * 
	 */
	private int numberOfRovers = 2;
	/**
	 * 
	 */
	private Color[] searchingColors = new Color[numberOfRovers];
	/**
	 * 
	 */
	private Environment environment;
	/**
	 * 
	 */
	private RoverFactory roverFactory;
	/**
	 * 
	 */
	private EnvironmentGrid grid = null;

	/**
	 * 
	 */
	private int state = 0;
	/**
	 * 
	 */
	private Task[] tasks;
	/**
	 * 
	 */
	private Observer[] observers;
	/**
	 * 
	 */
	private List<Observer> observersList = new ArrayList<Observer>();
	/**
	 * 
	 */
	private List<Rover> rovers = new ArrayList<Rover>();
	
	
	/**
	 * declare static variable called instance for Singleton Pattern in CentralStation
	 */
	private static CentralStation instance;
	/**
	 * maxRunTime in seconds. Default 10 minutes  
	 */
	private int maxRunTime = 10 * 60;
	/**
	 * 
	 */
	private Scanner reader = new Scanner(System.in);
	
	/**
	 * 
	 */
	private CentralStation() {
		this.roverFactory = new RoverFactory();
	}

	/**
	 * initalizes grid
	 * @return         
	 */
	private void initGrid() {
		//minus 5 because of the walls that make the area smaller
		int environmentSize = environment.getWorldSize() - 5;		
		this.grid = new EnvironmentGrid(environmentSize, environmentSize);
		
	}
	

	private void initTasks(){
		this.tasks = new Task[this.numberOfRovers];
		this.tasks[0] = new Task(this.searchingColors[0], this.grid.getPathOne(), this.grid.getPathOne().get(0), orientation.EAST);
		this.tasks[1] = new Task(this.searchingColors[1], this.grid.getPathTwo(), this.grid.getPathTwo().get(0), orientation.WEST);
	}

	/**
	 * 
	 */
	private void spawnRovers() {	
		for (Task task : this.tasks){ 
			Rover rover = this.roverFactory.createRover(task, this.searchingColors);
			this.environment.add(rover);
			this.rovers.add(rover);			
		}
	}
	
	private void initBoxes(int numberOfColors){	
		if(numberOfColors == 2){
		this.environment.makeBox(new Vector3d(2, 0, -2), new Vector3f(1.1f, 2, 1.1f), searchingColors[0]);
		this.environment.makeBox(new Vector3d(-2, 0, 2), new Vector3f(1.1f, 2, 1.1f), searchingColors[0]);
		this.environment.makeBox(new Vector3d(-2, 0, -2), new Vector3f(1.1f, 2, 1.1f), searchingColors[1]);
		this.environment.makeBox(new Vector3d(2, 0, 2), new Vector3f(1.1f, 2, 1.1f), searchingColors[1]); 
		}
		if(numberOfColors == 1){
		this.environment.makeBox(new Vector3d(2, 0, -2), new Vector3f(1.1f, 2, 1.1f), searchingColors[0]);
		this.environment.makeBox(new Vector3d(-2, 0, 2), new Vector3f(1.1f, 2, 1.1f), searchingColors[0]);
		this.environment.makeBox(new Vector3d(-2, 0, -2), new Vector3f(1.1f, 2, 1.1f), searchingColors[0]);
		this.environment.makeBox(new Vector3d(2, 0, 2), new Vector3f(1.1f, 2, 1.1f), searchingColors[0]); 
		}
		
	}

	/**
	 * checking life time of first rover. If the lifetime exceeds the maximum run time, then the central station stops the all rovers.
	 */
	private void checkRovers() {

		
		if(this.checkFinishedTasks()){
			System.out.println("The pathes have been covered");
			// stop timer task
			this.cancel();
			this.stopRovers();	
		}
		
		else if (this.rovers.get(0).getLifeTime() >= this.maxRunTime){
			System.out.println("Max Run time exceeded. Stop the rovers");
			// stop timer task
			this.cancel();
			this.stopRovers();			
		}	
		
	
	}
	
	private boolean checkFinishedTasks() {		
		for(Rover rover: this.rovers){			
			if(!rover.finishedTask()){
				return false;
			}
		}
		
		return true;	
	}

	/**
	 * Stop all the rovers
	 * @param roverId         
	 */
	private void stopRovers() {
		this.setState(1);
	}

	@Override
	public int getState() {
		return this.state;
	}

	@Override
	public void setState(int state) {
		this.state = state;
		this.notifyEverybody();		
	}

	@Override
	public void attach(Observer observer) {
		this.observersList.add(observer);		
	}

	@Override
	public void detach(Observer observer) {
		this.observersList.remove(observersList.indexOf(observer));	
	}

	@Override
	public void notifyEverybody() {
		for (Observer observer : this.observersList){ observer.update(); }		
	}
	
	/**
	 * Instantiates and returns the only instance of the CentralStation class
	 * @return    
	 */
	public static CentralStation getInstance() {
		if (instance == null) {
			instance = new CentralStation();
		}
		return instance;
	}

	/**
	 * 
	 * @param numberOfRovers 
	 * @param searchingColors 
	 * @param environment 
	 */
	public void init(Environment environment) {
		System.out.println("How many different colored boxes are you looking for? (choose between 1, 2)");
		int numberOfColors = reader.nextInt();
		for (int i = 0; i < numberOfColors; i++){
			System.out.println("What is color " + (i + 1) + "? choose between red, blue, green");
			String color = reader.next();
			switch (color) {
		        case "red": searchingColors[i] = Color.red; 
		        break;
		        case "blue": searchingColors[i] = Color.blue;
		        break;
		        case "green": searchingColors[i] = Color.green;
		        break;
			}	
		}
		
		this.searchingColors[0] = Color.blue;
		this.searchingColors[1] = Color.red;
			
		this.environment = environment; 	

		this.initBoxes(this.searchingColors.length);
		this.initGrid();
		this.initTasks();
		this.spawnRovers();
		
	}

	@Override
	public void run() {
		this.checkRovers();
	}

}