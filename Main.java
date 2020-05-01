package ROBOSEARCH;

import java.util.Timer;
import simbad.gui.Simbad;

public class Main {
	
	public static void main(String[] args) {

		// request antialising so that diagonal lines are not "stairy"
		System.setProperty("j3d.implicitAntialiasing", "true");

		Environment environment = new Environment(); 	
		Timer timer = new Timer();
		
		//get the only instance of the central station
		CentralStation cs = CentralStation.getInstance();
		cs.init(environment);
		
		timer.schedule(cs, 0, 1000);
		
		// Set-up Simbad environment
		Simbad platform = new Simbad(environment, false); 					
		platform.update(platform.getGraphics());
		
		System.out.println("----------------------------------------");
	}

}
