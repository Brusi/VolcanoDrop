package com.retrom.volcano.control;

import com.badlogic.gdx.Gdx;

public class ControlManager {
	public static AbstractControl keyboard = new KeyboardControl();
	public static AbstractControl tiltAndTap = new TiltAndTapPhoneControl();
	public static AbstractControl onScreen = new OnScreenPhoneControl();
	
	public static AbstractControl getControl() {
		return currentControl;
	}
	
	public static void setControl(AbstractControl newControl) {
		currentControl = newControl;
	}
	
	private static AbstractControl currentControl;

	public static void init() {
		switch (Gdx.app.getType()) {
		case Android:
		case iOS:
			currentControl = onScreen;
			break;
		case Applet:
		case Desktop:
		case WebGL:
			currentControl = keyboard;
//			currentControl = onScreen;
			break;
		case HeadlessDesktop:
		default:
			break;
		}
	} 
}
