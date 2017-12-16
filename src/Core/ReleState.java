package Core;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ReleState")
public class ReleState {

	@XStreamAsAttribute
	public int number;

	@XStreamAsAttribute
	public boolean state;
	

	public ReleState(int releNumber, boolean releState) {		
		number = releNumber;
		state = releState;
	}

	public String getCommand() {
		String cmd = state ? "ON" : "OFF";
		return String.format("rel %s %s", number, cmd);
	}

}
