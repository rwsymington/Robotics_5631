//Main branch
package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Joystick driver;
	Talon rightMotor1, rightMotor2, leftMotor1, leftMotor2;
	BuiltInAccelerometer acc;

	private double limiter;
	private int timer;
	private boolean b = false;
	private double leftM, rightM;

	// gyro
	private Gyro gyro;
	private int gyroInput = 1;
	private double adjust;
	private boolean enabled;

	// Auto
	RobotDrive robot;
	private boolean execute;
	private Encoder e;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		driver = new Joystick(0); // passing in port of the joystick
		leftMotor1 = new Talon(0);
		leftMotor2 = new Talon(1);
		rightMotor1 = new Talon(2); // port is based on roboRio pwm ports
		rightMotor2 = new Talon(3); // port is based on roboRio pwm ports
		limiter = 1;
		acc = new BuiltInAccelerometer();
		timer = 0;
		leftM = 1;
		rightM = 1;
		// gyro
		gyro = new Gyro(gyroInput);
		adjust = 0;
		enabled = false;
		// Auto
		robot = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
		execute = false;
		// e.getR
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

	}

	/**
	 * This function is called periodically during operator control Simple tank
	 * drive
	 */
	public void teleopPeriodic() {// Set the gyro

		double throttle = driver.getRawAxis(3);// will cap the max speed
		double t = ((1 + -throttle) / 2); // determine the limiting value from

		leftMotor1.set(driver.getRawAxis(2) * t);
		leftMotor2.set(driver.getRawAxis(2) * t);
		rightMotor1.set(driver.getRawAxis(2) * t);
		rightMotor2.set(driver.getRawAxis(2) * t);

		if (driver.getRawButton(1)) {// set gyro
			adjust = gyro.getAngle();
		}
	}

	public void command() {
		if (!execute) {
			if (driver.getRawButton(1)) {
				execute = true;
				timer = 0;
			}
		} else {
			timer++;
			if (timer < 60)
				robot.drive(0.2, 0);
			if (timer > 60 && timer < 120)
				robot.drive(0.2, 1);
			if (timer > 120) {
				execute = false;
			}
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		// System.out.println("Gyro ~ "+ gyro.getAngle());
		timer++;
		if (timer > 60) {//Once a sec
			if (driver.getRawButton(10)) {//Check if this button is pressed
				if (enabled) { // enable or disable the gyro
					enabled = false;
				} else {
					enabled = true;
				}
			}
			timer = 0;
		}

		double xAxis = driver.getRawAxis(0);

		double forward = driver.getRawAxis(1); // Front back
		double throttle = driver.getRawAxis(3);// will cap the max speed
		double t = ((1 + -throttle) / 2); // determine the limiting value from
											// 0-1
		if (driver.getRawButton(1)) {// brake (trigger button)
			// set speeds to zero
			leftMotor1.set(0);
			leftMotor2.set(0);
			rightMotor1.set(0);
			rightMotor2.set(0);
			System.out.println("Brake");
		} else if (!enabled) { // normal mode

			if (xAxis < -0.1) {// left
				leftM = 1 - Math.abs(xAxis);// slow down the left tracks
				rightM = 1;
			} else if (xAxis > 0.1) {// right
				rightM = 1 - Math.abs(xAxis);// slow down the right side
				leftM = 1;
			} else {
				leftM = 1;
				rightM = 1;
			}

			leftMotor1.set(-forward * t * leftM); // move the left wheels
			leftMotor2.set(-forward * t * leftM);
			rightMotor1.set((forward) * t * rightM); // move the right wheels
			rightMotor2.set((forward) * t * rightM);
			if (forward > -0.1 && forward < 0.1) {
				if (driver.getRawAxis(2) < -0.1 || driver.getRawAxis(2) > 0.1) {
					leftMotor1.set(driver.getRawAxis(2) * t);
					leftMotor2.set(driver.getRawAxis(2) * t);
					rightMotor1.set(driver.getRawAxis(2) * t);
					rightMotor2.set(driver.getRawAxis(2) * t);
				}
			}
		} else { // gyro mode
			//Limit the X Axis motors to turn the robot
			if (getX() < -0.1) {// left
				leftM = 1 - Math.abs(getX());// slow down the left side
				rightM = 1;
			} else if (getX() > 0.1) {// right
				rightM = 1 - Math.abs(getX());// slow down the right side
				leftM = 1;
			} else {
				leftM = 1;
				rightM = 1;
			}
			
			leftMotor1.set(-getY() * t * leftM); // move the left wheels
			leftMotor2.set(-getY() * t * leftM);
			rightMotor1.set(getY() * t * rightM); // move the right wheels
			rightMotor2.set(getY() * t * rightM);
			if (getY() > -0.1 && getY() < 0.1) {//this is debatable
				if (driver.getRawAxis(2) < -0.1 || driver.getRawAxis(2) > 0.1) {//Check if the driver would like to turn
					leftMotor1.set(driver.getRawAxis(2) * t);
					leftMotor2.set(driver.getRawAxis(2) * t);
					rightMotor1.set(driver.getRawAxis(2) * t);
					rightMotor2.set(driver.getRawAxis(2) * t);
				}
			}
		}
		if (timer == 60) {// data output
			System.out.println("Gyro Enabled ~ " + enabled + "\tY Axis ~ " + getY() + "\tX Axis ~ " + getX());
		}
	}

	// Swerve Drive - first try? #itMightNotWork

	public double getX() { // xAxis = 0 yAxis = 1
		double theta1 = Math.atan(driver.getRawAxis(0) / driver.getRawAxis(1));
		double h = driver.getRawAxis(1) / Math.sin(theta1);
		double thetaX = (adjust - gyro.getAngle()) + theta1;
		return (h * Math.sin(thetaX));
		// return ((driver.getRawAxis(1) /
		// Math.sin(Math.atan(driver.getRawAxis(0)/driver.getRawAxis(1))))*Math.sin((adjust-gyro.getAngle())+(Math.atan(driver.getRawAxis(0)/driver.getRawAxis(1)))));
	}

	public double getY() {
		double theta2 = Math.atan(driver.getRawAxis(1) / driver.getRawAxis(0));
		double h = driver.getRawAxis(1) / Math.sin(theta2);
		double thetaY = theta2 - (adjust - gyro.getAngle());
		return (h * Math.sin(thetaY));
		// return ((driver.getRawAxis(1) /
		// Math.sin(Math.atan(driver.getRawAxis(1)/driver.getRawAxis(0)))*Math.sin((Math.atan(driver.getRawAxis(1)/driver.getRawAxis(0)))
		// - (adjust-gyro.getAngle()))));
	}

}
