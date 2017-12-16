package Core;

import jssc.SerialPort;
import jssc.SerialPortException;

public class OpticState {

	public static boolean line_1;
	public static boolean line_2;
	public static boolean line_3;
	public static boolean line_4;
	public static boolean line_5;
	public static boolean line_6;
	public static boolean line_7;
	public static boolean line_8;
	public static boolean breakSignal;	
	

	public static void setState(String motor, boolean state) {
		String lineId = "line_" + motor;		
		try {
			OpticState.class.getDeclaredField(lineId).set(null, state);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public static boolean getState(String motor) {
		boolean state = false;
		String lineId = "line_" + motor;
		try {
			state = (boolean) OpticState.class.getDeclaredField(lineId).get(
					null);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}
	

	public static void waitBreakOff() {
		try {
			SerialPortHelper.getInstance().serialPort.addEventListener(
					new BreakLineListner(), SerialPort.MASK_RXCHAR);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void clearListner(BreakLineListner reader) {
		try {
			SerialPortHelper.getInstance().serialPort.removeEventListener();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader = null;
	}

}

class BreakLineListner extends SerialPortReader {	

	@Override
	public void parseMessage(String answer) {
		if (answer.contains("BL")) {
			OpticState.breakSignal = false;
			OpticState.clearListner(this);
		}
		
	}

}
