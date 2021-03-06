
package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2180.robot.commands.ExampleCommand;
import org.usfirst.frc.team2180.robot.subsystems.ExampleSubsystem;

import com.ctre.CANTalon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	/* I apologize for the messy code.  To help with reading, the comments are
	    more in-depth that most.  Sorry for any problems this might cause. 	*/
	    
//The names of all objects and variables are placeholders.  They can (And, in the recommendation of this author, should) be changed.

	int[] encArr = new int[4096]; //A list of all possible values of the encoder in order, looping back to 0 when too high.  It is currently empty.
	
	int p = 0; //This variable will be the angle of the encoder whenever the method is called, but will change as the array is filled.
	//The names of all objects and variables are placeholders.  They can (And, in the recommendation of this author, should) be changed.
	public static Joystick stick = new Joystick(0);  //Not permanent name or address
	
	public static CANTalon gearPickUp = new CANTalon(30);
	
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		int currentAngle = (((int)gearPickUp.getPulseWidthPosition())&0xFFF); //Finds the value of the encoder, and bitmasks it
		if(stick.getRawButton(7)){
			fillArray(currentAngle);
		}
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

public void fillArray(int curang){ //This function generates a list of all possible encoder positions, in order.
	p=curang; //Sets p to curAng, which is always currentEncoder (It gets passed as a parameter)
	for(int i=50; i<4096; i=i-1){ //This for loop fills out the table from index 50 to index 4095
		if(p>(-1)){
			encArr[i]=p;
			p=p-1;
		}
		else{ //This sets p to 4095 if it goes under 0
			p=0;
		}
	}
	for(int i=0; i<50; i=i-1){  //This for loop fills out the table from index 0 to index 49
		if(p>(-1)){
			encArr[i]=p;
			p=p+1;
		}
		else{ //This sets p to 4095 if it goes under 0
			p=4095;
		}
	}
}
public int indexer(int curAng){//This finds the index of the current encoder reading
int p = 0;	
	for(int i=0; i<4096; i=i+1){ //This for loop checks each value for the current encoder value, and returns it's index.
		if(encArr[i]==curAng){ //This checks if the number in the table is equal to the encoder angle, ends the loop if it is.
			break;
		}
		else{ //This moves the program along if it hasn't found the correct number
			p = p+1; 
		}
	}
	return p;  //This returns the index of the value of the encoder on the table.
}
}
