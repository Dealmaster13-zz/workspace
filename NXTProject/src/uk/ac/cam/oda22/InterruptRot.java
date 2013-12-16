package uk.ac.cam.oda22;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class InterruptRot {

	public static void main(String[] args) {
		// Display "Program 4" in row 0 of the LCD.
		LCD.drawString("Program 4", 0, 0);

		// Rotate the motor four complete revolutions backwards.
		Motor.A.rotate(360 * -4, true);
		
		// Display the tachometer reading on the LCD while the motor is rotating.
		while (Motor.A.isMoving() && Button.readButtons() == 0) {
			LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
			
			Delay.msDelay(10);
		}

		// Stop the motor if it is moving.
		if (Motor.A.isMoving()) {
			Motor.A.stop();
		}

		// Display the tachometer reading on the LCD.
		LCD.drawInt(Motor.A.getTachoCount(), 0, 2);
		
		// Wait until a button is pressed.
		Button.waitForAnyPress();
	}

}
