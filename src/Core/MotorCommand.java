package Core;

import Configuration.Configuration;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("MotorCommand")
public class MotorCommand {

	@XStreamAsAttribute
	@XStreamAlias("number")
	public String motor;

	@XStreamAsAttribute
	@XStreamAlias("dir")
	public String dir;

	@XStreamAsAttribute
	@XStreamAlias("steps")
	public Integer step;

	@XStreamOmitField
	public boolean isRun;

	public MotorCommand(String motorNumber, Boolean direction, Integer stepCount) {
		motor = motorNumber;
		dir = direction ? "L" : "R";
		step = stepCount;
		isRun = false;
	}

	public String getCommand(int commandType) {
		return commandType == 1 ? String.format("run %s %s %s", motor, dir,
				step) : String.format("go %s %s", motor, step);
	}

	public void setZero() {
		SerialPortHelper.getInstance().Write(String.format("sz %s", motor));
	}

	public int getLengthStepMin() {
		return (int) Configuration.getParametr("lengthmin_" + motor, false);
	}

}
