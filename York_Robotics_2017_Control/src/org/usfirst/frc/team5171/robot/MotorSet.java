package org.usfirst.frc.team5171.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;

public class MotorSet implements PIDOutput {

	/**
	 * Motors in set
	 */
	public ArrayList<SpeedController> motors;
	
	public MotorSet() {
		
		this.motors = new ArrayList<SpeedController>();
		
	}
	
	/**
	 * Add motor to motor set
	 * @param motor The SpeedController to add
	 */
	public void add(SpeedController motor) {
		
		this.motors.add(motor);
		
	}
	
	/**
	 * Set output of motor set manually
	 * @param output The value from -1 to 1 to set the motors
	 */
	public void set(double output) {
		
		pidWrite(output);
		
	}
	
	/**
	 * Write output to all motors in set
	 */
	public void pidWrite(double output) {
		
		for (SpeedController motor:motors) {
			
			motor.set(output);
			
		}

	}

}
