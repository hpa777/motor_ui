package Configuration;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class UserKey {

	@XStreamAsAttribute
	public String name;
	
	@XStreamAsAttribute
	public String prog;
	
}
