package kinectproject;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class JavaKinectProclipsingExample extends PApplet {

	SimpleOpenNI context;

	public void setup() {

		context = new SimpleOpenNI(this);

		// enable depthMap generation
		context.enableDepth();

		// enable skeleton generation for all joints
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

	}

	public void draw() {
		context.update();
		image(context.depthImage(),0,0);

		int i;

		for (i = 0; i <= 10; i++) {
			// check if the skeleton is being tracked
			if (context.isTrackingSkeleton(i)) {
				drawSkeleton(i);
				updateSkelPos(i);
			}
		}

	}

	public void updateSkelPos(int userId) {
		// get 3D position of a joint

		PVector positionReelPartieDuCorps = new PVector();
		PVector positionPartieDuCorps = new PVector();

		context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_HEAD,
				positionReelPartieDuCorps);
		
		context.convertRealWorldToProjective(positionReelPartieDuCorps,
				positionPartieDuCorps);
		
		System.out.println("Head: x=" + (context.depthWidth()
				- positionPartieDuCorps.x) + " y=" + positionPartieDuCorps.y
				+ " z=" + positionPartieDuCorps.z);

	}

	// draw the skeleton with the selected joints
	public void drawSkeleton(int userId) {
		
		// draw limbs
		context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT); 
		

	}

	public void onNewUser(int userId) {
		System.out.println("New User Detected - userId: " + userId);

		// start pose detection
		context.startPoseDetection("Psi", userId);
	}

	public void onLostUser(int userId) {
		System.out.println("User Lost - userId: " + userId);
	}

	public void onStartPose(String pose, int userId) {
		System.out.println("Start of Pose Detected  - userId: " + userId
				+ ", pose: " + pose);

		// stop pose detection
		context.stopPoseDetection(userId);

		// start attempting to calibrate the skeleton
		context.requestCalibrationSkeleton(userId, true);
	}

	// when calibration begins
	public void onStartCalibration(int userId) {
		System.out.println("Beginning Calibration - userId: " + userId);
	}

	// when calibaration ends - successfully or unsucessfully
	public void onEndCalibration(int userId, boolean successfull) {
		System.out.println("Calibration of userId: " + userId
				+ ", successfull: " + successfull);

		if (successfull) {
			System.out.println("  User calibrated !!!");
			// begin skeleton tracking
			context.startTrackingSkeleton(userId);
		} else {
			System.out.println("  Failed to calibrate user !!!");

			// Start pose detection
			context.startPoseDetection("Psi", userId);
		}
	}

}
