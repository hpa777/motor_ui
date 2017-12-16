package Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import Configuration.Configuration;
import Configuration.SerialPortSettings;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialPortHelper {

	private static SerialPortHelper instance = new SerialPortHelper();

	public static SerialPortHelper getInstance() {
		return instance;
	}

	protected static String LF;
	
	public PrintStream log;

	private SerialPortHelper() {
		byte[] lf = { 0x0d };
		LF = new String(lf);
		File file = new File("log.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log = new PrintStream(fos);	
		
		initPort();
	}

	public SerialPort serialPort;

	public boolean IsPortOpen() {
		return serialPort != null && serialPort.isOpened();
	}

	public boolean IsControllerConect;
	
	public void ClearEvent(){
		try {
			serialPort.removeEventListener();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			
		}
	}

	public void initPort() {
		SerialPortSettings settings = Configuration.current.serialPortSettings;
		if (settings == null)
			return;
		serialPort = new SerialPort(settings.Name);
		try {
			serialPort.openPort();
			serialPort.setParams(settings.Baudrate, settings.Databits,
					settings.Stopbits, settings.Parity);
			int flowConrolMask = 0;
			switch (settings.Flowcontrol) {
			case 0:
				flowConrolMask = SerialPort.FLOWCONTROL_NONE;
				break;
			case 1:
				flowConrolMask = SerialPort.FLOWCONTROL_RTSCTS_IN
						| SerialPort.FLOWCONTROL_RTSCTS_OUT;
				break;
			case 2:
				flowConrolMask = SerialPort.FLOWCONTROL_XONXOFF_IN
						| SerialPort.FLOWCONTROL_XONXOFF_OUT;
				break;
			}
			serialPort.setFlowControlMode(flowConrolMask);			
			
		//	IsControllerConect = serialPort.readString().toLowerCase().contains("usb-step-motor");

		} catch (SerialPortException e) {
			IsControllerConect = false;
			// LoggerEngine.logger.log(Level.SEVERE, "Exception: ", e);
		}
		
	}

	public void closePort() {
		if (IsPortOpen()) {
			try {
				synchronized (monitor) {
					serialPort.purgePort(1);
					serialPort.purgePort(2);
					serialPort.closePort();
					serialPort = null;
				}
			} catch (SerialPortException e) {
				// LoggerEngine.logger.log(Level.SEVERE, "Exception: ", e);
			}

		}
	}

	public final Object monitor = new Object();

	public void Write(String command) {
		if (IsPortOpen()) {
			String cmd = command + LF;
			try {
		//		synchronized (monitor) {
					serialPort.writeString(cmd);					
					if(command.contains("run")){
						log.println(command);
					}
		//			monitor.notifyAll();
		//		}

			} catch (SerialPortException e) {
				// LoggerEngine.logger.log(Level.SEVERE, "Exception: ", e);
			}
		}
		
	}
	
	public static int crc16(byte[] bytes) { 
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
             }
        }
        crc &= 0xffff;        
        return crc;
    }	
	
	
	public static byte[] intToByteArray(int a) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(a).array();
	}

}

