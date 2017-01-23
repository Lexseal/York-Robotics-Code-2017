package org.usfirst.frc.team5171.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

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
					
					String read = buffer.readLine();
					
					read = read.substring(read.indexOf('$'), read.indexOf('*'));
					
					this.error = calculateError(Double.parseDouble(read));
					
				}
				
				else {
					
					this.error = 0;
					
				}
				
			}
			
		} catch (Exception e) { e.printStackTrace(); }
		
	}
	
	/**
	 * 
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
