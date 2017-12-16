package Configuration;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class MotorParams {
	
	@XStreamAsAttribute
	public String name;
	
	@XStreamAsAttribute
	public Boolean visible;
	
	@XStreamAsAttribute
	public Boolean sensor;
	
	@XStreamAsAttribute
	public Integer lengthmin;

}
