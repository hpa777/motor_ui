package Configuration;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import javax.swing.KeyStroke;

import org.jnativehook.keyboard.NativeKeyEvent;

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
				value = (param != null && param.lengthmin != null) ? param.lengthmin : 0; 
				
			} else if (params.paramField.equals("hot")) {
				int keyCode = (int) field.get(current);
				if (keyCode != 0) {					
					if (!isCheckBox) {
						value = NativeKeyEvent.getKeyText(keyCode);
					}					
					else {
						value = KeyStroke.getKeyStroke(keyCode, InputEvent.CTRL_MASK);						
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

}

class Params {

	String id;

	String paramField;

	public Params(String _id) {
		id = _id;
		paramField = null;
		String[] i = id.split("_");
		if (i[0].contains("key")) {
			id = i[0] + "_" + i[1];
			paramField = i[2];
		} else if (i[0].contains("sensor") || i[0].contains("lengthmin")) {
			id = "motor" + "_" + i[1];
			paramField = i[0];
		} else if (i.length == 1) {
			paramField = "hot";
		}
	}
}
