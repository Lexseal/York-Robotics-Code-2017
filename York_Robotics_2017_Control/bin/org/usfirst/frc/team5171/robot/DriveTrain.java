package org.usfirst.frc.team5171.robot;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * Drive train control class
 * @author Kevin
 *
 */
public class DriveTrain implements PIDOutput {
	
	public MotorSet motorSetLeft, motorSetRight;
	
	public double setThrottle;
	
	public DriveTrain(MotorSet motorSetLeft, MotorSet motorSetRight) {
		
		this.motorSetLeft = motorSetLeft;
		this.motorSetRight = motorSetRight;
		
		this.setThrottle = 0;
		
	}
	
	/**
	 * Drive robot with a given throttle and rotation, with default square throttle on
	 * @param throttle The throttle value from -1 to 1
	 * @param rotation The rotation value from -1 to 1
	 */
	public void drive(double throttle, double rotation) {
		
		this.drive(throttle, rotation, true);
		
	}
	
	public void drive(double throttle, double rotation, boolean squareThrottle) {
		
		// Square throttle
		if (squareThrottle) throttle = throttle >= 0 ? Math.pow(throttle, 2) : -Math.pow(throttle, 2);
				
		// Set motor set values
		motorSetLeft.set(throttle + rotation);
		motorSetRight.set(-throttle + rotation);
		
	}

	public void pidWrite(double output) {
		
		this.drive(setThrottle, output);
		
	}

}
