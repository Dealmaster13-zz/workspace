package uk.ac.cam.oda22.core.robots.actions;

import java.util.List;

import uk.ac.cam.oda22.core.ListFunctions;
import uk.ac.cam.oda22.core.MathExtended;

/**
 * @author Oliver
 *
 */
public class RotateAction implements IRobotAction {

	public final double rads;

	public RotateAction(double rads) {
		this.rads = rads;
	}

	@Override
	public void addAction(List<IRobotAction> l) {
		IRobotAction lastAction = l.size() == 0 ? null : ListFunctions.getLast(l);
		
		if (lastAction != null && lastAction instanceof RotateAction) {
			double newAngle = MathExtended.normaliseAngle(((RotateAction) lastAction).rads + this.rads);
			
			RotateAction newAction = new RotateAction(newAngle);
			
			ListFunctions.removeLast(l);
			l.add(newAction);
		}
		else {
			l.add(this);
		}
	}
	
	@Override
	public String toString() {
		return "Rotate action: " + this.rads;
	}
	
}
