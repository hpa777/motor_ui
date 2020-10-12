package Core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import jssc.SerialPort;
import jssc.SerialPortException;
import Configuration.Configuration;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import net.java.balloontip.BalloonTip;

public class ProgramStep implements MouseWheelListener {

	public List<MotorCommand> motorsCmd;

	public List<ReleState> releStates;

	public String description;

	public Integer frequency;

	public Integer accel;

	public Integer stepType; // 0-координата 1-шаги

	public Integer stepCount;

	public Integer cycleCount;

	public Integer blockNumber;

	public Boolean isStop;

	public ProgramStep() {
		runMotor = false;
		stepType = Configuration.current.programType;
		stepPanel = getStepForm();
		refreshMotorsPanel();
		// refreshHotKeys();
		prt_part1 = 0;
	}

	@XStreamOmitField
	public Integer prt_part1;

	@XStreamOmitField
	public JPanel stepPanel;

	@XStreamOmitField
	private JPanel motors_panel;

	@XStreamOmitField
	private JPanel task_panel;

	@XStreamOmitField
	private JTextField descriptionField;

	public void refreshMotorsPanel() {
		for (Component component : motors_panel.getComponents()) {
			String name = component.getName();
			if (name == null) {
				continue;
			}
			String className = component.getClass().getName();
			if (className.contains("JLabel") && name.contains("motor_")) {
				((JLabel) component).setText((String) Configuration
						.getParametr(name, false));
				component.setVisible((Boolean) Configuration.getParametr(name,
						true));
				continue;
			}
			if (className.contains("CheckBox")) {
				if (name.contains("rele_")) {
					((JCheckBox) component).setText((String) Configuration
							.getParametr(name, false));
					component.setVisible((Boolean) Configuration.getParametr(
							name, true));
				} else if (name.contains("motor_")) {
					boolean v = (Boolean) Configuration.getParametr(name, true);
					component.setVisible(v);
					String[] n = name.split("_");
					Component chb = components.get("rev_" + n[1]);
					chb.setVisible(v && stepType == 1);
					chb.setEnabled(Configuration.current.canEdit);
				}
				component.setEnabled(Configuration.current.canEdit);
			}
		}
		for (Component component : task_panel.getComponents()) {
			String className = component.getClass().getName();
			if (className.contains("JFormattedTextField")
					|| className.contains("CheckBox")) {
				component.setEnabled(Configuration.current.canEdit);
			}
		}
		descriptionField.setEditable(Configuration.current.canEdit);
		label_z2.setVisible(stepType == 1);
	}

	@XStreamOmitField
	private Boolean runMotor;

	@XStreamOmitField
	private HashMap<String, Component> components;

	private Object getValueByName(String name, Boolean commitEdit) {
		Component component = components.get(name);
		String className = component.getClass().getName();
		if (className.contains("FormattedTextField")) {
			if (commitEdit) {
				try {
					((JFormattedTextField) component).commitEdit();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Object value = ((JFormattedTextField) component).getValue();
			if (value.getClass().getName().toLowerCase().contains("long")) {
				value = ((Long) value).intValue();
			}
			return value == null ? 0 : value;
		} else if (className.contains("CheckBox")) {
			return ((JCheckBox) component).isSelected();
		} else if (className.contains("JTextField")) {
			return ((JTextField) component).getText();
		}
		return null;
	}

	private void setValueByName(String name, Object value) {
		if (value == null) {
			return;
		}
		Component component = components.get(name);
		String className = component.getClass().getName();
		if (className.contains("FormattedTextField")) {

			((JFormattedTextField) component).setValue(value);
		} else if (className.contains("CheckBox")) {
			((JCheckBox) component).setSelected((Boolean) value);
		} else if (className.contains("JTextField")) {
			((JTextField) component).setText((String) value);
		}
	}

	public void IncreaseStepLength() {
		getEnabledMotors();
		if (motorsCmd.size() == 0)
			return;
		frequency = (Integer) getValueByName("Frequency", true);
		if (frequency > 0) {
			setValueByName("Frequency", frequency + 1);
		}
	}

	public void DecreaseStepLength() {
		int minStepLength = getEnabledMotors();
		if (motorsCmd.size() == 0)
			return;
		frequency = (Integer) getValueByName("Frequency", true);
		if (frequency > 0 && frequency > minStepLength) {
			setValueByName("Frequency", frequency - 1);
		}
	}

	private int getEnabledMotors() {
		motorsCmd = new ArrayList<MotorCommand>();
		int minStepLength = 0;
		for (Component component : motors_panel.getComponents()) {
			String name = component.getName();
			if (name == null
					|| !component.getClass().getName().contains("CheckBox")) {
				continue;
			}
			String[] n = name.split("_");
			if (n.length == 2 && n[0].equals("motor")
					&& ((JCheckBox) component).isSelected()) {
				Boolean dir = (Boolean) getValueByName("rev_" + n[1], false);
				Integer step = (Integer) getValueByName("StepCount", false);
				MotorCommand cmd = new MotorCommand(n[1], dir, step);
				int msl = cmd.getLengthStepMin();
				if (msl > minStepLength) {
					minStepLength = msl;
				}
				motorsCmd.add(cmd);
				int mn = 8 + (Integer.parseInt(n[1]) - 1) * 2;
				prt_part1 |= 1 << mn;
				if (dir) {
					prt_part1 |= 1 << mn + 1;
				}

			}
		}
		return minStepLength;
	}

	private void setEnableMotor(String number, String dir) {
		for (Component component : motors_panel.getComponents()) {
			String name = component.getName();
			if (name == null
					|| !component.getClass().getName().contains("CheckBox")) {
				continue;
			}
			String[] n = name.split("_");
			if (n.length == 2 && n[0].equals("motor") && n[1].equals(number)) {
				((JCheckBox) component).setSelected(true);
				if (dir.equals("L")) {
					setValueByName("rev_" + n[1], true);
				}
				break;
			}
		}
	}

	private void getEnabledReles() {
		releStates = new ArrayList<ReleState>();
		for (int i = 1; i < 5; i++) {
			boolean s = (boolean) getValueByName(String.format("Rele_%s", i),
					false);
			releStates.add(new ReleState(i, s));
			if (s) {
				prt_part1 |= 1 << i + 3;
			}
		}

	}

	public byte[] updateModel(boolean commitEdit) {
		frequency = (Integer) getValueByName("Frequency", commitEdit);
		accel = (Integer) getValueByName("Accel", commitEdit);
		stepCount = (Integer) getValueByName("StepCount", commitEdit);
		cycleCount = (Integer) getValueByName("CycleCount", commitEdit);
		blockNumber = (Integer) getValueByName("BlockNumber", commitEdit);
		description = (String) getValueByName("Description", commitEdit);
		isStop = (Boolean) getValueByName("IsStop", false);
		prt_part1 = stepType;
		if (isStop) {
			prt_part1 |= 1 << 1;
		}
		getEnabledMotors();
		getEnabledReles();
		byte[] buf = new byte[24];
		System.arraycopy(SerialPortHelper.intToByteArray(prt_part1), 0, buf, 0, 4);
		System.arraycopy(SerialPortHelper.intToByteArray(frequency), 0, buf, 4, 4);
		System.arraycopy(SerialPortHelper.intToByteArray(stepCount), 0, buf, 8, 4);
		System.arraycopy(SerialPortHelper.intToByteArray(accel), 0, buf, 12, 4);
		System.arraycopy(SerialPortHelper.intToByteArray(cycleCount), 0, buf, 16, 4);
		System.arraycopy(SerialPortHelper.intToByteArray(blockNumber), 0, buf, 20, 4);		
		return buf;
	}

	public void updateView() {
		if (stepPanel == null) {
			runMotor = false;
			stepPanel = getStepForm();
		}
		comboBox.setSelectedIndex(stepType);
		setValueByName("Frequency", frequency);
		setValueByName("Accel", accel);
		setValueByName("StepCount", stepCount);
		setValueByName("CycleCount", cycleCount);
		setValueByName("BlockNumber", blockNumber);
		setValueByName("Description", description);
		setValueByName("IsStop", isStop);
		if (motorsCmd != null) {
			for (MotorCommand command : motorsCmd) {
				setEnableMotor(command.motor, command.dir);
			}
		}
		if (releStates != null) {
			for (ReleState releState : releStates) {
				setValueByName(String.format("Rele_%s", releState.number),
						releState.state);
			}
		}
		refreshMotorsPanel();
		// refreshHotKeys();
	}

	/*
	 * public void refreshHotKeys() { //
	 * task_panel.getInputMap(JTabbedPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
	 * ).clear(); MainWindow.addHotKey("runBlock", task_panel); }
	 */
	@XStreamOmitField
	private JLabel label_z2;

	@XStreamOmitField
	private JButton runMotorButton;

	@XStreamOmitField
	private JFormattedTextField madeStepsField;

	public void runMotorOff(int sc) {
		runMotor = false;
		runMotorButton.setIcon(null);		
		if (sc > 0) {
			Integer c = (Integer) getValueByName("MadeSteps", false);
			setValueByName("MadeSteps", c + sc);
		} else {
			setValueByName("MadeSteps", 0);
		}
	}
	
	public void keyPressed(String dir) {
		if (!runMotor || Program.busy) {
			return;
		}		
		if (!dir.isEmpty()) {
			int notches = dir.equals("L") ? -10 : 10;
			for (MotorCommand command : motorsCmd) {
				String cmd = String.format("wm %s %s %s",
						command.motor, dir, 10);				
				SerialPortHelper.getInstance().Write(cmd);
			}
			Integer c = (Integer) getValueByName("MadeSteps", false);
			setValueByName("MadeSteps", c + notches);
		}
	}

	@SuppressWarnings("serial")
	class CheckBox extends JCheckBox {

		public CheckBox(String arg0) {
			super(arg0);
			this.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					Program.needSave = true;
				}
			});
		}
	}

	@SuppressWarnings("serial")
	class FormattedTextField extends JFormattedTextField {

		public FormattedTextField() {
			super();
			// TODO Auto-generated constructor stub
		}

		public FormattedTextField(AbstractFormatter formatter) {
			super(formatter);
			// TODO Auto-generated constructor stub
		}

		public FormattedTextField(AbstractFormatterFactory factory,
				Object currentValue) {
			super(factory, currentValue);
			// TODO Auto-generated constructor stub
		}

		public FormattedTextField(AbstractFormatterFactory factory) {
			super(factory);
			// TODO Auto-generated constructor stub
			this.addPropertyChangeListener("value",
					new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							// TODO Auto-generated method stub
							Program.needSave = true;

						}
					});
		}

		public FormattedTextField(Format format) {
			super(format);
			// TODO Auto-generated constructor stub
		}

		public FormattedTextField(Object value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

	}

	@SuppressWarnings("serial")
	public JPanel getStepForm() {
		components = new HashMap<>();
		JPanel parent_panel = new JPanel();

		parent_panel.addMouseWheelListener(this);
		parent_panel.setLayout(null);

		motors_panel = new JPanel();

		motors_panel.setBounds(12, 39, 641, 89);
		parent_panel.add(motors_panel);
		GridBagLayout gbl_motors_panel = new GridBagLayout();
		gbl_motors_panel.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		gbl_motors_panel.columnWidths = new int[] { 64, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0 };
		gbl_motors_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_motors_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		motors_panel.setLayout(gbl_motors_panel);

		JLabel label_1 = new JLabel();
		label_1.setName("motor_1");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 0;
		motors_panel.add(label_1, gbc_label_1);

		JLabel label_2 = new JLabel();
		label_2.setName("motor_2");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 2;
		gbc_label_2.gridy = 0;
		motors_panel.add(label_2, gbc_label_2);

		JLabel label_3 = new JLabel();
		label_3.setName("motor_3");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 3;
		gbc_label_3.gridy = 0;
		motors_panel.add(label_3, gbc_label_3);

		JLabel label_4 = new JLabel();
		label_4.setName("motor_4");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 4;
		gbc_label_4.gridy = 0;
		motors_panel.add(label_4, gbc_label_4);

		JLabel label_5 = new JLabel();
		label_5.setName("motor_5");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.insets = new Insets(0, 0, 5, 5);
		gbc_label_5.gridx = 5;
		gbc_label_5.gridy = 0;
		motors_panel.add(label_5, gbc_label_5);

		JLabel label_6 = new JLabel();
		label_6.setName("motor_6");
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.insets = new Insets(0, 0, 5, 5);
		gbc_label_6.gridx = 6;
		gbc_label_6.gridy = 0;
		motors_panel.add(label_6, gbc_label_6);

		JLabel label_7 = new JLabel();
		label_7.setName("motor_7");
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.insets = new Insets(0, 0, 5, 5);
		gbc_label_7.gridx = 7;
		gbc_label_7.gridy = 0;
		motors_panel.add(label_7, gbc_label_7);

		JLabel label_8 = new JLabel();
		label_8.setName("motor_8");
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.insets = new Insets(0, 0, 5, 5);
		gbc_label_8.gridx = 8;
		gbc_label_8.gridy = 0;
		motors_panel.add(label_8, gbc_label_8);

		JLabel label_z1 = new JLabel("\u041C\u043E\u0442\u043E\u0440\u044B");
		GridBagConstraints gbc_label_z1 = new GridBagConstraints();
		gbc_label_z1.insets = new Insets(0, 0, 5, 5);
		gbc_label_z1.gridx = 0;
		gbc_label_z1.gridy = 1;
		motors_panel.add(label_z1, gbc_label_z1);

		CheckBox checkBox_1_1 = new CheckBox("1");
		checkBox_1_1.setName("motor_1");
		checkBox_1_1.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_1 = new GridBagConstraints();
		gbc_checkBox_1_1.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_1.gridx = 1;
		gbc_checkBox_1_1.gridy = 1;
		motors_panel.add(checkBox_1_1, gbc_checkBox_1_1);

		CheckBox checkBox_1_2 = new CheckBox("2");
		checkBox_1_2.setName("motor_2");
		checkBox_1_2.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_2 = new GridBagConstraints();
		gbc_checkBox_1_2.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_2.gridx = 2;
		gbc_checkBox_1_2.gridy = 1;
		motors_panel.add(checkBox_1_2, gbc_checkBox_1_2);

		CheckBox checkBox_1_3 = new CheckBox("3");
		checkBox_1_3.setName("motor_3");
		checkBox_1_3.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_3 = new GridBagConstraints();
		gbc_checkBox_1_3.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_3.gridx = 3;
		gbc_checkBox_1_3.gridy = 1;
		motors_panel.add(checkBox_1_3, gbc_checkBox_1_3);

		CheckBox checkBox_1_4 = new CheckBox("4");
		checkBox_1_4.setName("motor_4");
		checkBox_1_4.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_4 = new GridBagConstraints();
		gbc_checkBox_1_4.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_4.gridx = 4;
		gbc_checkBox_1_4.gridy = 1;
		motors_panel.add(checkBox_1_4, gbc_checkBox_1_4);

		CheckBox checkBox_1_5 = new CheckBox("5");
		checkBox_1_5.setName("motor_5");
		checkBox_1_5.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_5 = new GridBagConstraints();
		gbc_checkBox_1_5.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_5.gridx = 5;
		gbc_checkBox_1_5.gridy = 1;
		motors_panel.add(checkBox_1_5, gbc_checkBox_1_5);

		CheckBox checkBox_1_6 = new CheckBox("6");
		checkBox_1_6.setName("motor_6");
		checkBox_1_6.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_6 = new GridBagConstraints();
		gbc_checkBox_1_6.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_6.gridx = 6;
		gbc_checkBox_1_6.gridy = 1;
		motors_panel.add(checkBox_1_6, gbc_checkBox_1_6);

		CheckBox checkBox_1_7 = new CheckBox("7");
		checkBox_1_7.setName("motor_7");
		checkBox_1_7.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_7 = new GridBagConstraints();
		gbc_checkBox_1_7.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_7.gridx = 7;
		gbc_checkBox_1_7.gridy = 1;
		motors_panel.add(checkBox_1_7, gbc_checkBox_1_7);

		CheckBox checkBox_1_8 = new CheckBox("8");
		checkBox_1_8.setName("motor_8");
		checkBox_1_8.setFocusable(false);
		GridBagConstraints gbc_checkBox_1_8 = new GridBagConstraints();
		gbc_checkBox_1_8.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_1_8.gridx = 8;
		gbc_checkBox_1_8.gridy = 1;
		motors_panel.add(checkBox_1_8, gbc_checkBox_1_8);

		CheckBox checkBox_r1 = new CheckBox("");
		checkBox_r1.setName("rele_1");
		checkBox_r1.setFocusable(false);
		components.put("Rele_1", checkBox_r1);
		GridBagConstraints gbc_checkBox_r1 = new GridBagConstraints();
		gbc_checkBox_r1.insets = new Insets(0, 0, 5, 5);
		gbc_checkBox_r1.gridx = 9;
		gbc_checkBox_r1.gridy = 1;
		motors_panel.add(checkBox_r1, gbc_checkBox_r1);

		CheckBox checkBox_r3 = new CheckBox("");
		checkBox_r3.setName("rele_3");
		checkBox_r3.setFocusable(false);
		components.put("Rele_3", checkBox_r3);
		GridBagConstraints gbc_checkBox_r3 = new GridBagConstraints();
		gbc_checkBox_r3.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox_r3.gridx = 10;
		gbc_checkBox_r3.gridy = 1;
		motors_panel.add(checkBox_r3, gbc_checkBox_r3);

		label_z2 = new JLabel("\u0420\u0435\u0432\u0435\u0440\u0441");
		GridBagConstraints gbc_label_z2 = new GridBagConstraints();
		gbc_label_z2.insets = new Insets(0, 0, 0, 5);
		gbc_label_z2.gridx = 0;
		gbc_label_z2.gridy = 2;
		motors_panel.add(label_z2, gbc_label_z2);

		CheckBox checkBox_2_1 = new CheckBox("1");
		checkBox_2_1.setFocusable(false);
		components.put("rev_1", checkBox_2_1);
		GridBagConstraints gbc_checkBox_2_1 = new GridBagConstraints();
		gbc_checkBox_2_1.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_1.gridx = 1;
		gbc_checkBox_2_1.gridy = 2;
		motors_panel.add(checkBox_2_1, gbc_checkBox_2_1);

		CheckBox checkBox_2_2 = new CheckBox("2");
		checkBox_2_2.setFocusable(false);
		components.put("rev_2", checkBox_2_2);
		GridBagConstraints gbc_checkBox_2_2 = new GridBagConstraints();
		gbc_checkBox_2_2.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_2.gridx = 2;
		gbc_checkBox_2_2.gridy = 2;
		motors_panel.add(checkBox_2_2, gbc_checkBox_2_2);

		CheckBox checkBox_2_3 = new CheckBox("3");
		checkBox_2_3.setFocusable(false);
		components.put("rev_3", checkBox_2_3);
		GridBagConstraints gbc_checkBox_2_3 = new GridBagConstraints();
		gbc_checkBox_2_3.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_3.gridx = 3;
		gbc_checkBox_2_3.gridy = 2;
		motors_panel.add(checkBox_2_3, gbc_checkBox_2_3);

		CheckBox checkBox_2_4 = new CheckBox("4");
		checkBox_2_4.setFocusable(false);
		components.put("rev_4", checkBox_2_4);
		GridBagConstraints gbc_checkBox_2_4 = new GridBagConstraints();
		gbc_checkBox_2_4.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_4.gridx = 4;
		gbc_checkBox_2_4.gridy = 2;
		motors_panel.add(checkBox_2_4, gbc_checkBox_2_4);

		CheckBox checkBox_2_5 = new CheckBox("5");
		checkBox_2_5.setFocusable(false);
		components.put("rev_5", checkBox_2_5);
		GridBagConstraints gbc_checkBox_2_5 = new GridBagConstraints();
		gbc_checkBox_2_5.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_5.gridx = 5;
		gbc_checkBox_2_5.gridy = 2;
		motors_panel.add(checkBox_2_5, gbc_checkBox_2_5);

		CheckBox checkBox_2_6 = new CheckBox("6");
		checkBox_2_6.setFocusable(false);
		components.put("rev_6", checkBox_2_6);
		GridBagConstraints gbc_checkBox_2_6 = new GridBagConstraints();
		gbc_checkBox_2_6.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_6.gridx = 6;
		gbc_checkBox_2_6.gridy = 2;
		motors_panel.add(checkBox_2_6, gbc_checkBox_2_6);

		CheckBox checkBox_2_7 = new CheckBox("7");
		checkBox_2_7.setFocusable(false);
		components.put("rev_7", checkBox_2_7);
		GridBagConstraints gbc_checkBox_2_7 = new GridBagConstraints();
		gbc_checkBox_2_7.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_7.gridx = 7;
		gbc_checkBox_2_7.gridy = 2;
		motors_panel.add(checkBox_2_7, gbc_checkBox_2_7);

		CheckBox checkBox_2_8 = new CheckBox("8");
		checkBox_2_8.setFocusable(false);
		components.put("rev_8", checkBox_2_8);
		GridBagConstraints gbc_checkBox_2_8 = new GridBagConstraints();
		gbc_checkBox_2_8.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_2_8.gridx = 8;
		gbc_checkBox_2_8.gridy = 2;
		motors_panel.add(checkBox_2_8, gbc_checkBox_2_8);

		CheckBox checkBox_r2 = new CheckBox("");
		checkBox_r2.setFocusable(false);
		checkBox_r2.setName("rele_2");
		components.put("Rele_2", checkBox_r2);
		GridBagConstraints gbc_checkBox_r2 = new GridBagConstraints();
		gbc_checkBox_r2.insets = new Insets(0, 0, 0, 5);
		gbc_checkBox_r2.gridx = 9;
		gbc_checkBox_r2.gridy = 2;
		motors_panel.add(checkBox_r2, gbc_checkBox_r2);

		CheckBox checkBox_r4 = new CheckBox("");
		checkBox_r4.setFocusable(false);
		checkBox_r4.setName("rele_4");
		components.put("Rele_4", checkBox_r4);
		GridBagConstraints gbc_checkBox_r4 = new GridBagConstraints();
		gbc_checkBox_r4.gridx = 10;
		gbc_checkBox_r4.gridy = 2;
		motors_panel.add(checkBox_r4, gbc_checkBox_r4);

		descriptionField = new JTextField();
		components.put("Description", descriptionField);
		descriptionField.setBounds(12, 12, 641, 20);
		parent_panel.add(descriptionField);
		descriptionField.setColumns(10);

		task_panel = new JPanel();
		/*
		 * ActionMap actionMap = task_panel.getActionMap();
		 * actionMap.put("runBlock", new AbstractAction() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * updateModel(false); runStep(); } });
		 */
		task_panel.setBounds(12, 152, 445, 148);
		parent_panel.add(task_panel);
		task_panel
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(71dlu;default)"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("57dlu"),
						FormFactory.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(33dlu;default)"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("max(35dlu;pref)"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel = new JLabel(
				"\u0414\u043B\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0441\u0442\u044C \u0448\u0430\u0433\u0430");
		task_panel.add(lblNewLabel, "2, 2, right, default");

		JLabel label = new JLabel(
				"\u0423\u0441\u043A\u043E\u0440\u0435\u043D\u0438\u0435");
		task_panel.add(label, "2, 4, right, default");

		comboBox = new JComboBox<String>();
		comboBox.setEnabled(false);
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"\u041A\u043E\u043E\u0440\u0434\u0438\u043D\u0430\u0442\u0430",
				"\u0428\u0430\u0433\u043E\u0432" }));
		task_panel.add(comboBox, "2, 6, right, default");
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stepType = comboBox.getSelectedIndex();
				refreshMotorsPanel();
			}
		});
		comboBox.setRenderer(new DefaultListCellRenderer() {

			@Override
			public void paint(Graphics g) {
				setForeground(Color.BLACK);
				super.paint(g);
			}
		});
		comboBox.setSelectedIndex(stepType);

		JLabel lblNewLabel_4 = new JLabel(
				"\u0428\u0430\u0433\u043E\u0432 \u0441\u0434\u0435\u043B\u0430\u043D\u043E");
		task_panel.add(lblNewLabel_4, "6, 6, right, default");

		JLabel lblNewLabel_2 = new JLabel(
				"\u041A\u043E\u043B-\u0432\u043E \u0446\u0438\u043A\u043B\u043E\u0432");
		task_panel.add(lblNewLabel_2, "2, 8, right, default");

		JLabel lblNewLabel_3 = new JLabel(
				"\u041D\u043E\u043C\u0435\u0440 \u0431\u043B\u043E\u043A\u0430");
		task_panel.add(lblNewLabel_3, "2, 10, right, default");

		NumberFormat opnDisplayFormat = NumberFormat.getIntegerInstance();
		opnDisplayFormat.setGroupingUsed(false);
		NumberFormat opnEditFormat = NumberFormat.getIntegerInstance();
		opnEditFormat.setGroupingUsed(false);
		NumberFormatter editFormatter = new NumberFormatter(opnEditFormat);
		// Set class of object returned from formatter
		editFormatter.setValueClass(Integer.class);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(
				new NumberFormatter(opnDisplayFormat), new NumberFormatter(
						opnDisplayFormat), editFormatter);

		final FormattedTextField stepCountField = new FormattedTextField(
				factory);
		stepCountField.setValue(new Integer(0));
		task_panel.add(stepCountField, "4, 6, left, default");
		components.put("StepCount", stepCountField);
		stepCountField.setColumns(10);

		madeStepsField = new JFormattedTextField(factory);
		madeStepsField.setEditable(false);
		
		madeStepsField.setValue(new Integer(0));
		task_panel.add(madeStepsField, "8, 6, fill, default");
		components.put("MadeSteps", madeStepsField);
		madeStepsField.setColumns(4);

		FormattedTextField accelField = new FormattedTextField(factory);
		accelField.setValue(new Integer(0));
		accelField.setColumns(10);
		task_panel.add(accelField, "4, 4, left, default");
		components.put("Accel", accelField);

		FormattedTextField cycleCountField = new FormattedTextField(factory);
		cycleCountField.setValue(new Integer(0));
		task_panel.add(cycleCountField, "4, 8, left, default");
		components.put("CycleCount", cycleCountField);
		cycleCountField.setColumns(10);

		FormattedTextField frequencyField = new FormattedTextField(factory);
		frequencyField.setInputVerifier(new FrequencyValidator());
		frequencyField.setValue(new Integer(0));
		task_panel.add(frequencyField, "4, 2, left, default");
		components.put("Frequency", frequencyField);
		frequencyField.setColumns(10);

		FormattedTextField blockNumberField = new FormattedTextField(factory);
		blockNumberField.setValue(new Integer(0));
		task_panel.add(blockNumberField, "4, 10, left, default");
		components.put("BlockNumber", blockNumberField);
		blockNumberField.setColumns(10);

		CheckBox stopCheckBox = new CheckBox("\u0421\u0442\u043E\u043F");
		stopCheckBox.setFocusable(false);
		stopCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckBox checkBox = (CheckBox)e.getSource();
				MainWindow.getInstance().setTabStopIcon(checkBox.isSelected());				
			}
		});
		task_panel.add(stopCheckBox, "6, 8, left, default");
		components.put("IsStop", stopCheckBox);

		JPanel button_panel = new JPanel();
		button_panel.setBounds(467, 152, 186, 148);
		parent_panel.add(button_panel);
		GridBagLayout gbl_button_panel = new GridBagLayout();
		gbl_button_panel.columnWidths = new int[] { 171 };
		gbl_button_panel.rowHeights = new int[] { 45, 45, 45 };
		gbl_button_panel.columnWeights = new double[] { 0.0 };
		gbl_button_panel.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		button_panel.setLayout(gbl_button_panel);

		runMotorButton = new JButton("Крутить мотор");
		runMotorButton.setFocusable(false);
		GridBagConstraints gbc_runMotorButton = new GridBagConstraints();
		gbc_runMotorButton.fill = GridBagConstraints.BOTH;
		gbc_runMotorButton.insets = new Insets(0, 0, 5, 0);
		gbc_runMotorButton.gridx = 0;
		gbc_runMotorButton.gridy = 0;
		button_panel.add(runMotorButton, gbc_runMotorButton);

		JButton setZeroButton = new JButton(
				"\u0423\u0441\u0442\u0430\u043D\u043E\u0432\u0438\u0442\u044C 0");
		setZeroButton.setFocusable(false);
		setZeroButton
				.setToolTipText("\u0423\u0441\u0442\u0430\u043D\u043E\u0432\u0438\u0442\u044C \u043B\u043E\u0433\u0438\u0447\u0435\u0441\u043A\u0438\u0439 \"0\" \u0432\u044B\u0431\u0440\u0430\u043D\u043D\u044B\u043C \u0434\u0432\u0438\u0433\u0430\u0442\u0435\u043B\u044F\u043C");
		setZeroButton.setVisible(stepType == 0);
		setZeroButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stepCountField.setValue(new Integer(0));
				updateModel(false);
				for (MotorCommand motor : motorsCmd) {
					motor.setZero();
				}
				JOptionPane.showMessageDialog(null, "Операция завершена",
						"Сообщение", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		GridBagConstraints gbc_setZeroButton = new GridBagConstraints();
		gbc_setZeroButton.fill = GridBagConstraints.BOTH;
		gbc_setZeroButton.insets = new Insets(0, 0, 5, 0);
		gbc_setZeroButton.gridx = 0;
		gbc_setZeroButton.gridy = 1;
		button_panel.add(setZeroButton, gbc_setZeroButton);

		JButton runBlockButton = new JButton("Выполнить блок");
		runBlockButton.setFocusable(false);
		GridBagConstraints gbc_runBlockButton = new GridBagConstraints();
		gbc_runBlockButton.fill = GridBagConstraints.BOTH;
		gbc_runBlockButton.gridx = 0;
		gbc_runBlockButton.gridy = 2;
		button_panel.add(runBlockButton, gbc_runBlockButton);
		runBlockButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateModel(false);				
				try {
					runStep();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		runMotorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateModel(false);
				if (!runMotor && !motorsCmd.isEmpty()) {
					runMotor = true;
					madeStepsField.setValue(new Integer(0));
					madeStepsField.requestFocus();
					runMotorButton.setIcon(new ImageIcon(
							ProgramStep.class
									.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
				} else {
					runMotor = false;
					runMotorButton.setIcon(null);
				}

			}
		});
		return parent_panel;
	}

	class FrequencyValidator extends InputVerifier {

		private BalloonTip toolTip;

		private void CreateToolTip(JComponent field, String message) {
			if (toolTip == null) {
				toolTip = new BalloonTip(field, message);
			} else {
				toolTip.setVisible(false);
				toolTip = new BalloonTip(field, message);
			}
		}

		@Override
		public boolean verify(JComponent input) {
			try {
				int value = Integer.parseInt(((JFormattedTextField) input)
						.getText());
				int minVal = getEnabledMotors();
				if (value > 0 && value < minVal) {
					CreateToolTip(input,
							String.format("Не может быть меньше %s", minVal));
					return false;
				}
			} catch (NumberFormatException ex) {
				CreateToolTip(input, "Можно вводить только цифры");
				return false;
			}
			if (toolTip != null) {
				toolTip.setVisible(false);
				toolTip = null;
			}
			return true;
		}

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (runMotor && !Program.busy) {
			int notches = e.getWheelRotation();
			String dir = notches > 0 ? "R" : "L";
			for (MotorCommand command : motorsCmd) {
				String cmd = String
						.format("wm %s %s %s", command.motor, dir, 1);
				SerialPortHelper.getInstance().Write(cmd);
			}
			Integer c = (Integer) getValueByName("MadeSteps", false);
			setValueByName("MadeSteps", c + notches);
		}
	}

	@XStreamOmitField
	private PortReader reader;
	@XStreamOmitField
	private JComboBox<String> comboBox;
	@XStreamOmitField
	private boolean isBlockRun;

	public void runStep() throws InterruptedException {
		if (Program.busy)
			return;
		Program.busy = true;
		SerialPortHelper instance = SerialPortHelper.getInstance();
		reader = new PortReader();
		try {
			instance.serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
			instance.serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
			instance.serialPort
					.addEventListener(reader, SerialPort.MASK_RXCHAR);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ReleState releState : releStates) {
			reader.sendCommand(releState.getCommand());
		}
		boolean needRun = false;
		for (MotorCommand command : motorsCmd) {
			/*
			 * if (OpticState.getState(command.motor) &&
			 * command.dir.equals("L")) { needRun = true; continue; }
			 */
			if (frequency > 0 && accel > 0) {
				reader.sendCommand(String.format("ac %s %s %s", command.motor,
						frequency, accel));
			} else if (frequency > 0) {
				reader.sendCommand(String.format("set %s %s", command.motor,
						frequency));
			}
			reader.sendCommand(command.getCommand(stepType));
			command.isRun = true;
			needRun = true;
		}
		isBlockRun = true;
		if (!needRun) {
			if (stepCount > 0 && frequency > 0) {
				reader.sendCommand(String.format("pause %s %s", stepCount,
						frequency));
				reader.sendCommand("run");
			} else {
				endStep();
			}
		} else {
			reader.sendCommand("run");
		}
		runMotorOff(stepCount);
	}

	public static final Object BlockComplet = new Object();

	public void endStep() {
		try {
			SerialPortHelper.getInstance().serialPort.removeEventListener();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader = null;
		Program.busy = false;
		synchronized (BlockComplet) {
			BlockComplet.notifyAll();
		}
		isBlockRun = false;
	}

	private class PortReader extends SerialPortReader {

		private Pattern stopReg;

		private Pattern opticLowReg;

		private Pattern opticHighReg;

		public PortReader() {

			stopReg = Pattern.compile("M(\\d)STOP (-?\\d*)");
			opticLowReg = Pattern.compile("D(\\d)L");
			opticHighReg = Pattern.compile("D(\\d)H");
		}

		private String findAnswer(Matcher matcher) {
			String motor = "";
			if (matcher.find()) {
				motor = matcher.group(1);
			}
			return motor;
		}

		@Override
		public void parseMessage(String answer) {
			// System.out.println(answer);

			if (answer.equals("BLOCK STOP") && isBlockRun) {
				endStep();
				return;
			}

			Matcher matcher = stopReg.matcher(answer);
			String motor = findAnswer(matcher);
			if (!motor.isEmpty()) {
				boolean flag = false;
				for (MotorCommand command : motorsCmd) {
					if (command.motor.equals(motor) && command.isRun) {
						command.isRun = false;
						int crd = Integer.parseInt(matcher.group(2));
						if (stepType == 0 && command.step != crd) {
							JOptionPane
									.showMessageDialog(
											null,
											String.format(
													"Мотор (%s) не пришел в заданную координату (%s)",
													motor, crd), "Ошибка",
											JOptionPane.ERROR_MESSAGE);
							isStop = true;
							endStep();
							return;
						}

					} else if (command.isRun) {
						flag = true;
					}
				}
				if (!flag) {
					endStep();
				}
				return;
			}

			matcher = opticLowReg.matcher(answer);
			motor = findAnswer(matcher);
			if (!motor.isEmpty()) {
				OpticState.setState(motor, false);
				return;
			}

			matcher = opticHighReg.matcher(answer);
			motor = findAnswer(matcher);
			if (!motor.isEmpty()) {
				OpticState.setState(motor, true);
				return;
			}

			if (answer.contains("PAUSE END")) {
				endStep();
				return;
			}

			if (answer.contains("BH")) {
				OpticState.breakSignal = true;
				endStep();
			}
			// System.out.println("Не распознал (" + answer + ")");
		}

	}
}