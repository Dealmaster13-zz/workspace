package uk.ac.cam.oda22;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class AccurateRotation {

	public static void main(String[] args) {
		// Display "Program 3" in row 0 of the LCD.
		LCD.drawString("Program 3", 0, 0);

		// Rotate the motor four complete revolutions.
		Motor.A.rotate(360 * 4);

		// Display the tachometer reading on the LCD (should be 1440).
		LCD.drawInt(Motor.A.getTachoCount(), 0, 1);

		// Rotate the motor to angle 0.
		Motor.A.rotateTo(0);

		// Display the tachometer reading on the LCD (should be 0).
		LCD.drawInt(Motor.A.getTachoCount(), 0, 2);
		
		// Wait until a button is pressed.
		Button.waitForAnyPress();
	}

}
