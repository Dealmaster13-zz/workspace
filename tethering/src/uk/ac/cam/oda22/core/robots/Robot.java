package uk.ac.cam.oda22.core.robots;

import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.tethers.Tether;

/**
 * @author Oliver
 *
 */
public abstract class Robot {

	public final Tether tether;
	
	public final double radius;
	
	/**
	 * The smallest fraction of a radian which can be recognised as a change in rotation
	 */
	public final double rotationalSensitivity;

	private Point2D position;

	/**
	 * In radians.
	 */
	private double rotation;
	
	private RobotOutline outline;
	
	public Robot(Point2D position, double radius, double rotation, double rotationalSensitivity, Tether tether) throws Exception {
		this.position = position;
		this.radius = radius;
		this.rotation = rotation;
		this.rotationalSensitivity = rotationalSensitivity;
		this.tether = tether;
		
		if (!tether.getLastPoint().equals(position)) {
			throw new Exception("Tether does not connect to robot.");
		}
	}

	public Point2D getPosition() {
		return position;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public RobotOutline getOutline() {
		return outline;
	}
	
	public void move(double dist) {
		double xChange = dist * Math.cos(rotation);
		double yChange = dist * Math.sin(rotation);
		
		this.position.setLocation(this.position.getX() + xChange, this.position.getY() + yChange);
		
		this.moveMotors(dist);
	}
	
	public void rotate(double rads) {
		this.rotation += rads;
		
		this.rotateMotors(rads / (2 * Math.PI));
	}
	
	protected void setOutline(RobotOutline outline) {
		this.outline = outline;
	}
	
	/**
	 * This method is for moving the robot in hardware.
	 * 
	 * @param dist
	 */
	protected void moveMotors(double dist) {
		// Leave blank for overriding.
	}
	
	/**
	 * This method is for rotating the robot in hardware.
	 * 
	 * @param revs
	 */
	protected void rotateMotors(double revs) {
		// Leave blank for overriding.
	}
	
}
