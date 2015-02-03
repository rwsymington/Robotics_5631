/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static Joystick driver;
	public static RobotDrive robot;
	public static double timer;
	
	Motor leftMotors;
	Motor rightMotors;
	
	Gyro gyro;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// Initialize all subsystems
		driver = new Joystick(0);
		leftMotors = new Motor(new Talon(1), new Talon(2), new Encoder(1,2, false, EncodingType.k4X)); // port is based on
		rightMotors = new Motor(new Talon(3), new Talon(4), new Encoder(3,4, true, EncodingType.k4X));
		
		gyro = new Gyro(1);
	}

	public void autonomousInit() {

		leftMotors.setSpeed(0.5);
		rightMotors.setSpeed(-0.5);
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		leftMotors.run();
		rightMotors.run();
		System.out.println(gyro.getAngle());
		gyro.reset();
	}

	public void teleopInit() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		log();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		
		double forward = driver.getRawAxis(1); //Front back
		double throttle = driver.getRawAxis(3);// will cap the max speed
		double t = ((1 + -throttle) / 2); // determine the limiting value from 0-1
		if(driver.getRawAxis(2) < -0.1 || driver.getRawAxis(2) > 0.1){
			
			robot.drive(1,0);
			
		}
		
	}

	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	private void log() {
		SmartDashboard.putString(null, "ddd");
	}
}
