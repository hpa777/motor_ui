package Configuration;

import static org.junit.Assert.*;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.junit.Test;

public class configurationTest {

	@Test
	public void testInit() {
		Configuration.init();
		assertNotNull(Configuration.current);		
	}

	@Test
	public void testSaveConfig() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGetMotorNameParametr() {
		for (int i = 1; i < 9 ; i++) {
			String field_id = "motor_" + Integer.toString(i);
			String field_value = "test" + Integer.toString(i);
			Configuration.setParametr(field_id, field_value);
			String value = (String)Configuration.getParametr(field_id, false);
			assertEquals(field_id, field_value, value);
		}		
	}
	@Test
	public void testSetGetMotorVisibleParametr(){
		for (int i = 1; i < 9; i++) {
			String field_id = "motor_" + Integer.toString(i);
			Configuration.setParametr(field_id, true);
			assertEquals("crash enable " + field_id, true, (Boolean)Configuration.getParametr(field_id, true));
			Configuration.setParametr(field_id, false);
			assertEquals("crash disable " + field_id, false, (Boolean)Configuration.getParametr(field_id, true));
		}
	}
	@Test
	public void testSetGetMotorSensorParametr(){
		for (int i = 1; i < 9; i++) {
			String field_id = "sensor_" + Integer.toString(i);
			Configuration.setParametr(field_id, true);
			assertEquals("crash enable " + field_id, true, (Boolean)Configuration.getParametr(field_id, true));
			Configuration.setParametr(field_id, false);
			assertEquals("crash disable " + field_id, false, (Boolean)Configuration.getParametr(field_id, true));
		}
	}
	@Test
	public void testSetGetMotorLengthMinParametr() {
		for (int i = 1; i < 9 ; i++) {
			String field_id = "lengthmin_" + Integer.toString(i);			
			Configuration.setParametr(field_id, Integer.toString(i));
			int value = (int)Configuration.getParametr(field_id, false);
			assertEquals(field_id, i, value);
		}		
	}
	@Test
	public void testSetGetLeftKeyParametr() {
		for (int i = 1; i < 9; i++) {
			String field_id = "leftkey_" + Integer.toString(i);
			Configuration.setParametr(field_id, i);
			String value = (String)Configuration.getParametr(field_id, false);
			assertEquals(field_id, NativeKeyEvent.getKeyText(i), value);
		}
	}
	@Test
	public void testSetGetRightKeyParametr() {
		for (int i = 1; i < 9; i++) {
			String field_id = "rightkey_" + Integer.toString(i);
			Configuration.setParametr(field_id, i);
			String value = (String)Configuration.getParametr(field_id, false);
			assertEquals(field_id, NativeKeyEvent.getKeyText(i), value);
		}
	}
	@Test
	public void testSetGetStepPerClickParametr() {
		for (int i = 1; i < 9 ; i++) {
			String field_id = "stepperclick_" + Integer.toString(i);			
			Configuration.setParametr(field_id, Integer.toString(i));
			int value = (int)Configuration.getParametr(field_id, false);
			assertEquals(field_id, i, value);
		}		
	}
	@Test
	public void testGetWorkDir() {
		fail("Not yet implemented");
	}

}
