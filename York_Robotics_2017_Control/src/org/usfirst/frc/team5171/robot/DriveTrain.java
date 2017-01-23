package org.usfirst.frc.team5171.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DriveTrain implements PIDOutput {
	
	public MotorSet motorSetLeft, motorSetRight;
	
	public double setThrottle;
	
	public DriveTrain(MotorSet motorSetLeft, MotorSet motorSetRight) {
		
		this.motorSetLeft = motorSetLeft;
		this.motorSetRight = motorSetRight;
		
		this.setThrottle = 0;
		
	}
	
	public void drive(double throttle, double rotation) {
		
		// Square throttle
		throttle = throttle>=0 ?
				Math.pow(throttle, 2)
				:-Math.pow(throttle, 2);
		
		// Set motor set values
		motorSetLeft.set(throttle/2 + rotation/2);
		motorSetRight.set(-throttle/2 + rotation/2);
		
	}

	public void pidWrite(double output) {
		
		this.drive(setThrottle, output);
		
	}

}
