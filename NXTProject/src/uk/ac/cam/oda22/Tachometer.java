package uk.ac.cam.oda22;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class Tachometer {

	public static void main(String[] args) {
		// Display "Program 2" in row 0 of the LCD.
		LCD.drawString("Program 2", 0, 0);

		// Set the motor speed to 2 rev/sec.
		Motor.A.setSpeed(360 * 2);
		
		// Run motor A in the forward direction.
		Motor.A.forward();

		// Wait for two seconds.
		Delay.msDelay(2000);

		// Display the motor angle on the LCD (should be less than 1440 due to inertia).
		LCD.drawInt(Motor.A.getTachoCount(), 0, 1);

		// Stop the motor.
		Motor.A.stop();

		// Display the tachometer reading on the LCD.
		LCD.drawInt(Motor.A.getTachoCount(), 0, 2);
		
		// Start the motor rotating backwards.
		Motor.A.backward();

		// Wait until the tacho count reaches 0.
		while (Motor.A.getTachoCount() > 0) {
			Delay.msDelay(1);
		}

		// Display the tachometer reading on the LCD.
		LCD.drawInt(Motor.A.getTachoCount(), 0, 3);

		// Stop the motor.
		Motor.A.stop();

		// Display the tachometer reading on the LCD.
		LCD.drawInt(Motor.A.getTachoCount(), 0, 4);
		
		// Wait until a button is pressed.
		Button.waitForAnyPress();
	}

}
