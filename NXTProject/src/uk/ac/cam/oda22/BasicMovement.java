package uk.ac.cam.oda22;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class BasicMovement {

	public static void main(String[] args) {
		// Display "Program 1" in row 0 of the LCD.
		LCD.drawString("Program 1", 0, 0);
		
		// Wait for a button to be pressed.
		Button.waitForAnyPress();
		
		// Run motor A in the forward direction.
		Motor.A.forward();
		
		// Display "FORWARD" in the top line.
		LCD.drawString("FORWARD", 0, 1);
		
		// Wait until a button is pressed.
		Button.waitForAnyPress();
		
		// Run the motor backward.
		Motor.A.backward();
		
		// Display "BACKWARD" in the next line.
		LCD.drawString("BACKWARD", 0, 2);
		
		// Wait until a button is pressed.
		Button.waitForAnyPress();
		
		// Stop the motor.
		Motor.A.stop();
	}

}
