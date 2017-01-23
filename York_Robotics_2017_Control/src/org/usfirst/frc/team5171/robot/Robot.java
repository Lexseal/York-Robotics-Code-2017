
package org.usfirst.frc.team5171.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Robot main class
 * @author 	Kevin Barnard
 * @since 	January 2017
 *
 */
public class Robot extends IterativeRobot {
    
    /**
     * Which control set to use;
     * 0 - XBOX
     */
    private final int CONTROL_SET = Map.XBOX;

    /**
     * Address for vision server
     */
    private final String VISION_ADDRESS = "RaspberryPi3";
    
    /**
     * Port for vision socket
     */
    private final int VISION_PORT = 1501;
    
	/**
	 * Deadband value
	 */
	private final double DEADBAND = 0.01;
	
    // Wheel motor sets and drive train
    MotorSet lWheels = new MotorSet(), rWheels = new MotorSet();
    DriveTrain driveTrain = new DriveTrain(lWheels, rWheels);
    
    // PID Controllers
    PIDController 
    	drivePID,
    	visionPID;
    
    // Gyroscope
    ADXRS450_Gyro gyro;
    
    // Vision
    Vision vision;
    
    // Controllers
    private Joystick 
		controllerDrive,
		controllerPanel;
    
    // Axes
	private double
		leftStickX 			= 0,
		leftStickY 			= 0,
		leftTrigger 		= 0,
		rightTrigger 		= 0,
		rightStickX 		= 0,
		rightStickY 		= 0;

	// Buttons
	private boolean 
		buttonA = false,
		buttonB = false,
		buttonX = false,
		buttonY = false,
		
		panelButton1 = false,
		panelButton2 = false,
		panelButton3 = false,
		panelButton4 = false;
	
    /**
     * Called once upon robot code initialization
     */
    public void robotInit() {
    	
    	// Clear SmartDashboard
    	Function.clearDashboard();
    	
    	// Initialize controllers
    	controllerDrive = new Joystick(Map.DRIVE_CONTROL);
    	controllerPanel = new Joystick(Map.PANEL_CONTROL);
    	
    	// Add wheel motors to drive train motor sets
        lWheels.add(new Talon(Map.MOTOR_L_1));
        lWheels.add(new Talon(Map.MOTOR_L_2));
        rWheels.add(new Talon(Map.MOTOR_R_1));
        rWheels.add(new Talon(Map.MOTOR_R_2));
        
        // Initialize gyroscope
        gyro = new ADXRS450_Gyro();
        
        // Initialize PID controllers
        drivePID = new PIDController(21, 2.5, 7, gyro, driveTrain);
        visionPID = new PIDController(0, 0, 0, vision, driveTrain);
        
        // Initialize vision
        vision = new Vision(VISION_ADDRESS, VISION_PORT);
        
    }

    /**
     * Called once upon autonomous mode initialization
     */
    public void autonomousInit() {
    	
    	// Clear SmartDashboard
    	Function.clearDashboard();
    	
    	// Start vision thread
    	vision.start();
    	
    	// Enable vision PID control
    	visionPID.enable();
    	
    	// Set vision PID setpoint
    	visionPID.setSetpoint(0);
    	
    }

    /**
     * Called periodically during autonomous mode
     */
    public void autonomousPeriodic() {
    	
    	SmartDashboard.putString("DB/String 0", Double.toString(visionPID.getError()));
    	
    }

    /**
     * Called once upon teleoperated mode initialization
     */
    public void teleopInit() {
    	
    	// Clear SmartDashboard
    	Function.clearDashboard();
    	
    	// Enable drive PID
    	drivePID.enable();
    	
    }
    
    /**
     * Called periodically during teleoperated mode
     */
    public void teleopPeriodic() {
    	
    	/*
    	 * Procedure:
    	 * 	1. Read controllers in control set
    	 * 	2. Eliminate deadband as necessary
    	 * 	3. Control drive train
    	 * 
    	 */
    	
    	// Read controller settings from selected control set
    	switch (CONTROL_SET) {
    	
    		// XBOX Controller (360 or One)
	    	case Map.XBOX:
	    		
	    		leftStickX = controllerDrive.getRawAxis(Map.XBOX_L_STICK_X);
		    	leftStickY = controllerDrive.getRawAxis(Map.XBOX_L_STICK_Y);
		    	leftTrigger = controllerDrive.getRawAxis(Map.XBOX_L_TRIGGER);
		    	rightTrigger = controllerDrive.getRawAxis(Map.XBOX_R_TRIGGER);
		    	rightStickX = controllerDrive.getRawAxis(Map.XBOX_R_STICK_X);
		    	rightStickY = controllerDrive.getRawAxis(Map.XBOX_R_STICK_Y);
		    	
		    	buttonA = controllerDrive.getRawButton(Map.XBOX_A);
		    	buttonB = controllerDrive.getRawButton(Map.XBOX_B);
		    	buttonX = controllerDrive.getRawButton(Map.XBOX_X);
		    	buttonY = controllerDrive.getRawButton(Map.XBOX_Y);
		    	
		    	panelButton1 = controllerPanel.getRawButton(Map.PANEL_BUTTON_1);
		    	panelButton2 = controllerPanel.getRawButton(Map.PANEL_BUTTON_2);
		    	panelButton3 = controllerPanel.getRawButton(Map.PANEL_BUTTON_3);
		    	panelButton4 = controllerPanel.getRawButton(Map.PANEL_BUTTON_4);
		    	
		    	break;
		    	
		    // Default if invalid
		    default:
		    	
		    	leftStickX = 0;
		    	leftStickY = 0;
		    	leftTrigger = 0;
		    	rightTrigger = 0;
		    	rightStickX = 0;
		    	rightStickY = 0;
		    	
		    	buttonA = false;
		    	buttonB = false;
		    	buttonX = false;
		    	buttonY = false;
		    	
		    	panelButton1 = false;
		    	panelButton2 = false;
		    	panelButton3 = false;
		    	panelButton4 = false;
		    	
		    	break;
    	
    	}
    	
    	// Eliminate deadband
    	leftStickX 		= Function.eliminateDeadband(leftStickX, DEADBAND);
    	leftStickY 		= Function.eliminateDeadband(leftStickY, DEADBAND);
    	leftTrigger 	= Function.eliminateDeadband(leftTrigger, DEADBAND);
    	rightTrigger	= Function.eliminateDeadband(rightTrigger, DEADBAND);
    	rightStickX 	= Function.eliminateDeadband(rightStickX, DEADBAND);
    	rightStickY 	= Function.eliminateDeadband(rightStickY, DEADBAND);
    	
    	// No-PID drive
    	if (!drivePID.isEnabled()) driveTrain.drive(-leftStickY, rightStickX);
    	
    	// PID drive
    	else {
    		
    		driveTrain.setThrottle = -leftStickY;
    		drivePID.setSetpoint(drivePID.getSetpoint() + rightStickX);
    		
    	}
        
    }

    /**
     * Called once upon test mode initialization
     */
    public void testInit() {
    	
    	// Clear SmartDashboard
    	Function.clearDashboard();
    	
    }
    
    /**
     * Called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
