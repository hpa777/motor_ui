package Core;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public abstract class SerialPortReader implements SerialPortEventListener {

	protected static final Object waitOk = new Object();

	protected static boolean ok = false;	

	public void sendCommand(String command) throws InterruptedException {
		ok = false;
		for (int i = 0; i < 3; i++) {			
			SerialPortHelper.getInstance().Write(command);
			synchronized (waitOk) {
				waitOk.wait(300);
			}
			if (ok)
				break;
		}
	}

	private StringBuilder message = new StringBuilder();

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() > 0) {
			SerialPortHelper sPort = SerialPortHelper.getInstance();
			try {
				for (byte b : sPort.serialPort.readBytes()) {
					// System.out.print((char) b);
					if (b == '\n')
						continue;
					if (b == '\r' && message.length() > 0) {
						if (message.toString().equals("OK")) {							
							ok = true;
							synchronized (waitOk) {
								waitOk.notifyAll();
							}
						} else {
							parseMessage(message.toString());
						}
						message.setLength(0);
					} else {
						message.append((char) b);
					}
				}

			} catch (SerialPortException e) {
				// LoggerEngine.logger.log(Level.SEVERE, "Exception: ", e);
			}

		}
	}

	public abstract void parseMessage(String answer);

}
