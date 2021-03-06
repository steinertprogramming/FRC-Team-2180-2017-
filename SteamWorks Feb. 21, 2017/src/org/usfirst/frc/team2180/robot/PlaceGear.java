package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class PlaceGear extends Command {

	public PlaceGear() {
		
	}
	
	public void execute() {
		Timer timer = new Timer();
		int pos = Robot.gearPickUp.getPulseWidthPosition() & 0xFFF;
		while (pos > 2506) {
			Robot.gearPickUp.set(-0.5);
		}
		
		timer.reset();
		timer.start();
		
		
	}
	
	protected boolean isFinished() {
		return true;
	}

}
