package org.usfirst.frc.team5171.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Vision stream reader and processor thread
 * @author Kevin
 *
 */
public class Vision extends Thread implements PIDSource {
	
	Socket socket;
	BufferedReader buffer;
	
	private double error;
	
	private static final double FOV = 54;
	private static final int CAMERA_WIDTH = 640;
	
	private PIDSourceType sourceType;
	
	public Vision(String address, int portNumber) {
		
		this.sourceType = PIDSourceType.kDisplacement;
		
		try {
			
			socket = new Socket(address, portNumber);
			buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) { e.printStackTrace(); }
		
	}
	
	/**
	 * Read stream and calculate error
	 */
	public void run() {
		
		try {
			
			while (true) {
				
				// Sleep thread 22 ms
				sleep((long) 22);
				
				if (buffer.readLine() != null) {
					
					// Read line from buffer
					String read = buffer.readLine();
					
					// Extract data from line
					read = read.substring(read.indexOf('$'), read.indexOf('*'));
					String[] parts = read.split(",");
					double[] data = new double[parts.length];
					for (int i = 0; i < parts.length; i++) data[i] = Double.parseDouble(parts[i]);
					
					// Calculate error from data
					this.error = calculateError(data[0]);
					
				} else
					this.error = 0;
				
			}
			
		} catch (Exception e) { e.printStackTrace(); }
		
	}
	
	/**
	 * Calculate the error in degrees of the target from the center of the screen
	 * @param center Pixel value of target center
	 * @return Error in degrees from center of screen. Positive values are right of center
	 */
	public double calculateError(double center) {
		
		return (center / CAMERA_WIDTH) * FOV - FOV/2;
		
	}
	
	public double getError() {
		
		return this.error;
		
	}

	public void setPIDSourceType(PIDSourceType pidSource) {

		this.sourceType = pidSource;
		
	}

	public PIDSourceType getPIDSourceType() {
		
		return this.sourceType;
		
	}

	public double pidGet() {
		
		return this.error;
		
	}

}
