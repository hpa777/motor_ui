package Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import jssc.SerialPort;
import jssc.SerialPortException;
import Core.*;

public class Config {

	public MotorParams motor_1;
	public MotorParams motor_2;
	public MotorParams motor_3;
	public MotorParams motor_4;
	public MotorParams motor_5;
	public MotorParams motor_6;
	public MotorParams motor_7;
	public MotorParams motor_8;
	public MotorParams rele_1;
	public MotorParams rele_2;
	public MotorParams rele_3;
	public MotorParams rele_4;

	public UserKey key_1;
	public UserKey key_2;
	public UserKey key_3;
	public UserKey key_4;
	public UserKey key_5;
	public UserKey key_6;
	public UserKey key_7;
	public UserKey key_8;
	public UserKey key_9;
	public UserKey key_10;
	public UserKey key_11;
	public UserKey key_12;
	public UserKey key_13;

	public SerialPortSettings serialPortSettings;

	public int freq;

	public String work_dir;

	public boolean canEdit;
	
	public int programType;
	
// hot keys
	public int startProg;
	public int runBlock;
	public int nextBlock;
	public int prevBlock;
	public int motorToLeft;
	public int motorToRight;

	@XStreamOmitField
	private int motor_sens_count;

	public void runMotorsToNull() {
		motor_sens_count = 0;		
		SerialPortHelper sHelper = SerialPortHelper.getInstance();
				
		for (Field field : Config.class.getDeclaredFields()) {
			if (field.getType() != MotorParams.class) {
				continue;
			}
			try {
				MotorParams motorParams = (MotorParams) field.get(this);
				if (motorParams.visible && motorParams.sensor) {
					String[] i = field.getName().split("_");
					if (motorParams.lengthmin > 0) {
						sHelper.Write(String.format("set %s %s", i[1],
								motorParams.lengthmin));
					}
					MotorCommand cmd = new MotorCommand(i[1], true, 20000);
					sHelper.Write(cmd.getCommand(1));
					motor_sens_count++;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// e.printStackTrace();
			}
		}
		
		if (motor_sens_count > 0) {
			try {
				sHelper.serialPort.addEventListener(
						new PortReader(), SerialPort.MASK_RXCHAR);
				sHelper.Write("run");
			} catch (SerialPortException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		

	}

	private class PortReader extends SerialPortReader {

		private Pattern opticHighReg;

		private int count;

		public PortReader() {
			count = 0;
			opticHighReg = Pattern.compile("D(\\d)H");
		}

		@Override
		public void parseMessage(String answer) {
			
			if (answer.contains("STOP")) {
				count++;
				if (motor_sens_count == count) {
					try {
						SerialPortHelper.getInstance().serialPort
								.removeEventListener();
					} catch (SerialPortException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Операция завершена",
							"Сообщение", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				Matcher matcher = opticHighReg.matcher(answer);
				if (matcher.find()) {
					SerialPortHelper.getInstance().Write(
							String.format("sz %s", matcher.group(1)));

				}
			}
		}

	}

}
