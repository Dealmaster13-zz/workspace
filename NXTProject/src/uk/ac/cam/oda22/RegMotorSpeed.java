package uk.ac.cam.oda22;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class RegMotorSpeed {

	public static void main(String[] args) {
		// Display "Program 5" in row 0 of the LCD.
		LCD.drawString("Program 5", 0, 0);

		// Set the motors speed to 2 rev/sec.
		Motor.A.setSpeed(360 * 2);
		Motor.B.setSpeed(360 * 2);
		Motor.C.setSpeed(360 * 2);

		for (int i = 0; i < 8; i++) {
			// Rotate the motors two complete revolutions.
			Motor.A.rotate(360 * 2, true);
			Motor.B.rotate(360 * 2, true);
			Motor.C.rotate(360 * 2, true);
	
			// Display the tachometer reading on the LCD while the motor is rotating.
			while (Motor.A.isMoving()) {
				int a = Motor.A.getTachoCount();
				int b = Motor.B.getTachoCount();
				int c = Motor.C.getTachoCount();

				LCD.clear((i % 6) + 1);
				LCD.drawInt(a, 0, (i % 6) + 1);
				LCD.drawInt(b, Integer.toString(a).length() + 1, (i % 6) + 1);
				LCD.drawInt(c, Integer.toString(a).length() + Integer.toString(b).length() + 2, (i % 6) + 1);
	
				Delay.msDelay(200);
			}
		}

		// Wait until a button is pressed.
		Button.waitForAnyPress();
	}

}
