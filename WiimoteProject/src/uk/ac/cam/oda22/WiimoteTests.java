package uk.ac.cam.oda22;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.physicalevents.ExpansionEvent;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.ClassicControllerRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.DisconnectionEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.GuitarHeroRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukInsertedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.NunchukRemovedEvent;
import wiiusej.wiiusejevents.wiiuseapievents.StatusEvent;

public class WiimoteTests implements WiimoteListener {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Wiimote[] wiimotes = WiiUseApiManager.getWiimotes(1, true);

		if (wiimotes.length == 0) {
			System.out.println("No Wiimotes found.");
		}
		else {
			Wiimote wiimote = wiimotes[0];
			wiimote.activateIRTRacking();
			wiimote.activateMotionSensing();
			wiimote.addWiiMoteEventListeners(new WiimoteTests());
		}
	}

	@Override
	public void onButtonsEvent(WiimoteButtonsEvent arg0) {
		System.out.println(arg0);
		
		if (arg0.isButtonAPressed()){
            WiiUseApiManager.shutdown();
        }
	}

	@Override
	public void onClassicControllerInsertedEvent(
			ClassicControllerInsertedEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onClassicControllerRemovedEvent(
			ClassicControllerRemovedEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onDisconnectionEvent(DisconnectionEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onExpansionEvent(ExpansionEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onIrEvent(IREvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onMotionSensingEvent(MotionSensingEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onNunchukInsertedEvent(NunchukInsertedEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onNunchukRemovedEvent(NunchukRemovedEvent arg0) {
		System.out.println(arg0);
	}

	@Override
	public void onStatusEvent(StatusEvent arg0) {
		System.out.println(arg0);
	}

}
