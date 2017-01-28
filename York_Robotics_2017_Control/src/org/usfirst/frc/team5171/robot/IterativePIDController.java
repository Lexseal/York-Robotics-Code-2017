package org.usfirst.frc.team5171.robot;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * Iterative PID controller. Shares functionality with wpilib PIDController class
 * @author Kevin
 *
 */
public class IterativePIDController {
	
	/**
	 * Proportional, Integral, and Derivative constants
	 */
	private double kP, kI, kD;

	/**
	 * Set point (target value) of control
	 */
	private double setpoint;
	
	private PIDSource source;
	private PIDOutput output;
	
	private double lastUpdateTime;
	
	private double lastError, integralError;
	
	private double tolerance;
	
	public IterativePIDController(double kP, double kI, double kD, PIDSource source, PIDOutput output) {
		
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		
		this.source = source;
		this.output = output;
		
		this.reset();
		
	}
	
	public void reset() {
		
		this.tolerance = this.integralError = this.lastError = this.lastUpdateTime = this.setpoint = 0;
		
	}
	
	public void setSetpoint(double setpoint) {
		
		this.setpoint = setpoint;
		
	}
	
	public void setTolerance(double tolerance) {
		
		this.tolerance = tolerance;
		
	}
	
	public void update() {
		
		// Get error and root error
		double error = this.getError(source.pidGet()),
				rootError = Math.sqrt(Math.abs(error));
		
		// Calculate change in time since last update in seconds
		double deltaT = (System.currentTimeMillis() - this.lastUpdateTime) / 1000;
		
		// Add current error multiplied by time to definite integral of error
		this.integralError += error*deltaT;
		
		// Calculate derivative as change in error over change in time since last update
		double derivativeError = (error-this.lastError)/deltaT;
		
		// Calculate output
		double output = 
				kP*((error > 0) ? rootError : -rootError) + 
				kI*this.integralError + 
				kD*derivativeError;
		
		// Check for tolerance and zero integral and error if within
		if (this.tolerance > 0 && Math.abs(error) < this.tolerance)
			this.integralError = output = 0;
		
		// Update last update time and error
		this.lastUpdateTime = System.currentTimeMillis();
		this.lastError = error;
		
		// Write output
		this.output.pidWrite(output);
		
	}
	
	public double getSetpoint() {
		
		return this.setpoint;
		
	}
	
	public double getError(double input) {
		
		return this.setpoint - input;
		
	}
	
	public double getTolerance() {
		
		return this.tolerance;
		
	}
	
	public double getkP() {
		return kP;
	}

	public void setkP(double kP) {
		this.kP = kP;
	}

	public double getkI() {
		return kI;
	}

	public void setkI(double kI) {
		this.kI = kI;
	}

	public double getkD() {
		return kD;
	}

	public void setkD(double kD) {
		this.kD = kD;
	}

}
