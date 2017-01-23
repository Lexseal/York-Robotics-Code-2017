package org.usfirst.frc.team5171.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Function {
	
	/**
	 * Eliminate deadband from a given input
	 * @param value The input value
	 * @param deadband The deadband value
	 * @return The deadband-eliminated value
	 */
	public static double eliminateDeadband(double value, double deadband) {
		
		if (Math.abs(value) >= deadband) return value;
		return 0;
		
	}
	
	/**
	 * Clear SmartDashboard fields
	 */
	public static void clearDashboard() {
		
		for (int i = 0; i < 10; i++) {
			
			SmartDashboard.putString("DB/String "+i, "");
			
		}
		
	}

}
