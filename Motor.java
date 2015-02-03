package org.usfirst.frc.team5631.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

public class Motor {
//	RobotDrive leftMotors = new RobotDrive(new Talon(0), new Talon(1));
//	RobotDrive rightMotors = new RobotDrive(new Talon(2), new Talon(3));
//	PID controller;
	private Talon left, right;
	private Encoder encoder; 
	private double wheel_size;
	private boolean stopped;
	
	double error, speed, target_speed, sum_error, net_error, diff_error, prev_error, power;
	double KP, KI, KD;
	
	int timer;
	
	public Motor(Talon front, Talon back, Encoder encoder){
		
		this.left = front;
		this.right = back;
		this.encoder = encoder;
		this.encoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
		this.encoder.setDistancePerPulse((4.0/*in*/*Math.PI)/(360.0*12.0/*in/ft*/));
		wheel_size = 0.10;
		power = 0;
		error = 0;
		speed = 0;
		target_speed = 0;
		sum_error = 0;
		net_error = 0;
		diff_error = 0;
		prev_error = 0;
		
		KP = 0.012;
		KI = 0;
		KD = 0;
		
		timer = 0;
	}
	
	public void setSpeed(double speed){
		

		this.speed = speed;
		
		if(speed > 0){
			power = 0.05;
		}else if(speed < 0){
			power = -0.05;
		}
	}
	
	public void run(){
		timer++;
		
		if(timer >= 10){
			loop();
	        timer = 0;
		}
		
		

		left.set(power);
		right.set(power);
	}
	public double constrain(double d){
		
		return d/6000;
		
	}
	
	public void loop(){
		double actualSpeed = encoder.getRate();
		target_speed = speed;
		
		error = target_speed - actualSpeed;

        sum_error = sum_error + error;
        sum_error = constrain(sum_error);

        //diff_error = error - prev_error;
        prev_error = error;
        //net_error = KP*error + KI*sum_error + KD*diff_error;
        net_error = KP * error;
		
        power = power + net_error;
        //power = constrain(power);

	}
	
	public void stop(){
		
	}
}
