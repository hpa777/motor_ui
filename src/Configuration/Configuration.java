package Configuration;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import javax.swing.KeyStroke;

import org.jnativehook.keyboard.NativeKeyEvent;

import Core.SerialPortHelper;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public final class Configuration {

	final static String configFileName = "config.xml";

	public static Config current;

	static {
		init();
	}

	public static void init() {
		File file = new File(configFileName);
		XStream xStream = new XStream(new DomDriver());
		xStream.processAnnotations(Config.class);
		try {
			current = (Config) xStream.fromXML(file);
		} catch (Exception e) {
			current = new Config();
		}
	}

	public static void saveConfig() {
		File file = new File(configFileName);
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(file, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (printWriter != null) {
			XStream xStream = new XStream(new DomDriver());
			xStream.processAnnotations(Config.class);
			printWriter.print(xStream.toXML(current));
			printWriter.close();
			file = null;
		}
	}

	public static void setParametr(String _id, Object value) {
		Params params = new Params(_id);
		try {
			Field field = current.getClass().getDeclaredField(params.id);
			if (params.id.equals("freq")) {
				field.set(current, Integer.parseInt((String) value));
			} else if (params.paramField == null) {
				MotorParams param = (MotorParams) field.get(current);
				if (param == null) {
					param = new MotorParams();
				}
				String typeName = value.getClass().getName();
				if (typeName.contains("java.lang.String")) {
					param.name = (String) value;
				} else if (typeName.contains("java.lang.Boolean")) {
					param.visible = (Boolean) value;
				}
				field.set(current, param);
			} else if (params.paramField.equals("sensor")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param == null) {
					param = new MotorParams();
				}
				param.sensor = (Boolean) value;
			} else if (params.paramField.equals("lengthmin")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param == null) {
					param = new MotorParams();
				}
				param.lengthmin = Integer.parseInt((String) value);
			} else if (params.paramField.equals("leftkey")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param == null) {
					param = new MotorParams();
				}
				param.leftkey = (Integer) value;
			} else if (params.paramField.equals("rightkey")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param == null) {
					param = new MotorParams();
				}
				param.rightkey = (Integer) value;
			} else if (params.paramField.equals("stepperclick")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param == null) {
					param = new MotorParams();
				}
				param.stepperclick = Integer.parseInt((String) value);
			} else if (params.paramField.equals("hot")) {
				field.set(current, value);
			} else {
				UserKey param = (UserKey) field.get(current);
				if (param == null) {
					param = new UserKey();
				}
				UserKey.class.getDeclaredField(params.paramField).set(param,
						value);
				field.set(current, param);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params = null;
	}

	public static Object getParametr(String _id, Boolean isCheckBox) {
		Params params = new Params(_id);
		Object value = !isCheckBox ? "" : false;
		try {
			Field field = current.getClass().getDeclaredField(params.id);
			if (params.paramField == null) {
				MotorParams param = (MotorParams) field.get(current);
				if (param != null) {
					value = !isCheckBox ? param.name : param.visible != null
							&& param.visible == true;
				}
			} else if (params.paramField.equals("sensor")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param != null && param.sensor != null) {
					value = param.sensor;
				}
			} else if (params.paramField.equals("lengthmin")) {
				MotorParams param = (MotorParams) field.get(current);
				value = (param != null && param.lengthmin != null) ? param.lengthmin
						: 0;

			} else if (params.paramField.equals("leftkey")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param.leftkey != null) {
					value = NativeKeyEvent.getKeyText(param.leftkey);
				}
			} else if (params.paramField.equals("rightkey")) {
				MotorParams param = (MotorParams) field.get(current);
				if (param.rightkey != null) {
					value = NativeKeyEvent.getKeyText(param.rightkey);
				}
			} else if (params.paramField.equals("stepperclick")) {
				MotorParams param = (MotorParams) field.get(current);
				value = (param != null && param.stepperclick != null) ? param.stepperclick
						: 0;

			} else if (params.paramField.equals("hot")) {
				int keyCode = (int) field.get(current);
				if (keyCode != 0) {
					if (!isCheckBox) {
						value = NativeKeyEvent.getKeyText(keyCode);
					} else {
						value = KeyStroke.getKeyStroke(keyCode,
								InputEvent.CTRL_MASK);
					}

				} else if (isCheckBox) {
					value = null;
				}
			} else {
				UserKey uKey = (UserKey) field.get(current);
				if (uKey != null) {
					value = UserKey.class.getDeclaredField(params.paramField)
							.get(uKey);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params = null;
		return value;
	}

	public static String getWorkDir() {
		return current.work_dir == null ? "." : current.work_dir;
	}

	public static boolean isHotKey(int keyCode) {		
		for (int i = 1; i <= 8; i++) {
			try {
				Field field = current.getClass().getDeclaredField(
						"motor_" + String.valueOf(i));
				MotorParams params = (MotorParams) field.get(current);
				if (!params.visible) {
					continue;
				}
				String dir = "";
				if (params.leftkey == keyCode) {
					dir = "L";
				} else if (params.rightkey == keyCode) {
					dir = "R";
				}
				if (!dir.isEmpty() && params.stepperclick != null) {					
					String cmd = String.format("wm %s %s %s", i, dir, params.stepperclick);
					SerialPortHelper.getInstance().Write(cmd);
					return true;
				}
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

}

class Params {

	String id;

	String paramField;

	public Params(String _id) {
		id = _id;
		paramField = null;
		String[] i = id.split("_");
		if (i[0].startsWith("key")) {
			id = i[0] + "_" + i[1];
			paramField = i[2];
		} else if (i[0].startsWith("sensor") || i[0].startsWith("lengthmin")
				|| i[0].startsWith("leftkey") || i[0].startsWith("rightkey")
				|| i[0].startsWith("stepperclick")) {
			id = "motor" + "_" + i[1];
			paramField = i[0];
		} else if (i.length == 1) {
			paramField = "hot";
		} else if (i[0].startsWith("mkey")) {

		}
	}
}
