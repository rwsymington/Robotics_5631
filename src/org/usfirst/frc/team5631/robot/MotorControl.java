package org.usfirst.frc.team5631.robot;

public class MotorControl {

	double power;

	public MotorControl() {
		power = 0;
	}

	public double run(double rpm, double actRpm, double otherRpm) {
		double dif = rpm - actRpm;
		double crossDif = actRpm - otherRpm;
		double inc = 0;
		if (Math.abs(dif) > 20) {
			inc = 0.1;
		} else if (Math.abs(dif) > 10) {
			inc = 0.05;
		} else if (Math.abs(dif) > 5) {
			inc = 0.025;
		} else if (Math.abs(dif) > 0.1) {
			inc = 0.001;
		}
		double m = Math.abs(crossDif) / crossDif;
		if (power >= 1 && m < 0) {
			if (Math.abs(crossDif) > 20) {
				power -= 0.02;
			} else if (Math.abs(crossDif) > 10) {
				power -= 0.01;
			} else if (Math.abs(crossDif) > 5) {
				power -= 0.005;
			} else if (Math.abs(crossDif) > 0.1) {
				power -= 0.00025;
			}
		} else {
			if (Math.abs(crossDif) > 20) {
				inc += 0.02 * m;
			} else if (Math.abs(crossDif) > 10) {
				inc += 0.01 * m;
			} else if (Math.abs(crossDif) > 5) {
				inc += 0.005 * m;
			} else if (Math.abs(crossDif) > 0.1) {
				inc += 0.00025 * m;
			}
		}

		if (dif > 0) {
			if (power + inc < 1)
				power += 0.1;
		} else if (dif < 0) {
			if (power - inc > -1)
				power -= 0.1;
		}
		return power;
	}

	public double getPower() {
		return power;
	}

}
