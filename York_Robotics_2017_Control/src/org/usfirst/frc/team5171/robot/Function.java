package org.usfirst.frc.team5171.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Basic static functions collection
 * @author Kevin
 *
 */
public class Function {
	
	/**
	 * Eliminate deadband from a given input
	 * @param value The input value
	 * @param deadband The deadband value
	 * @return The deadband-eliminated value
	 */
	public static double eliminateDeadband(double value, double deadband) {
		return Math.abs(value) < deadband ? 0 : value;
	}
	
	/**
	 * Clear SmartDashboard fields
	 */
	public static void clearDashboard() {
		
		for (int i = 0; i < 10; i++)
			SmartDashboard.putString("DB/String " + i, "");
		
	}

}
