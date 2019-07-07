package Configuration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import Core.SerialPortHelper;

import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@SuppressWarnings("serial")
public class ProgramSettingsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_dir;
	
	public static boolean isOpen;

	/**
	 * Launch the application.
	 */
	public static void show(String[] args) {
		try {
			ProgramSettingsDialog dialog = new ProgramSettingsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	
	
	public ProgramSettingsDialog() {
		isOpen = true;
		FocusAdapter motorNameAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				JTextField field = (JTextField)e.getSource();
				Configuration.setParametr(field.getName(), field.getText());						
			}
		};
		
		ItemListener motorVisibleListener = new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox checkBox = (JCheckBox)e.getSource();
				Configuration.setParametr(checkBox.getName(), checkBox.isSelected());
			}
		};
		
		final NativeKeyListener nativeKeyListener = new NativeKeyListener() {
			
			@Override
			public void nativeKeyPressed(NativeKeyEvent e) {
				// TODO Auto-generated method stub				
				Component component = getMostRecentFocusOwner();
				String name = component.getName();
				if(name.startsWith("leftkey") || name.startsWith("rightkey")) {
					int keyCode = e.getKeyCode();
					if (keyCode != 14) {
						Configuration.setParametr(name, keyCode);
						((JTextField)component).setText(NativeKeyEvent.getKeyText(keyCode));
					} else {
						Configuration.setParametr(name, null);
						((JTextField)component).setText("");
					}
				}
			}

			@Override
			public void nativeKeyReleased(NativeKeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void nativeKeyTyped(NativeKeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		GlobalScreen.addNativeKeyListener(nativeKeyListener);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GlobalScreen.removeNativeKeyListener(nativeKeyListener);
			}
		});
		KeyAdapter hotKeyAdapter = new KeyAdapter() {						
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub				
				e.consume();
			}
		};
		
		
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
		
		setType(Type.UTILITY);
		setTitle("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0430 \u043F\u0430\u0440\u0430\u043C\u0435\u0442\u0440\u043E\u0432");
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 941, 554);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(new Color(0, 0, 0)));
			panel.setBounds(6, 35, 571, 352);
			contentPanel.add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 65, 148, 14, 0, 82, 50, 50, 71, 0 };
			gbl_panel.rowHeights = new int[] { 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
					Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel label = new JLabel(
						"\u041D\u0430\u0437\u0432\u0430\u043D\u0438\u0435");
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.anchor = GridBagConstraints.NORTH;
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 1;
				gbc_label.gridy = 0;
				panel.add(label, gbc_label);
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
			}
			
			JLabel label_1 = new JLabel("\u0410\u043A\u0442\u0438\u0432\u0435\u043D");
			label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gbc_label_1 = new GridBagConstraints();
			gbc_label_1.anchor = GridBagConstraints.NORTH;
			gbc_label_1.insets = new Insets(0, 0, 5, 5);
			gbc_label_1.gridx = 2;
			gbc_label_1.gridy = 0;
			panel.add(label_1, gbc_label_1);
			
			JLabel lblNewLabel_2 = new JLabel("\u0414\u0430\u0442\u0447\u0438\u043A");
			lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.gridx = 3;
			gbc_lblNewLabel_2.gridy = 0;
			panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
			{
				JLabel lbln = new JLabel("<html>\u0414\u043B\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0441\u0442\u044C<br/>&nbsp;&nbsp;\u0448\u0430\u0433\u0430 (\u043C\u0438\u043D)</html>");
				lbln.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_lbln = new GridBagConstraints();
				gbc_lbln.anchor = GridBagConstraints.NORTH;
				gbc_lbln.insets = new Insets(0, 0, 5, 5);
				gbc_lbln.gridx = 4;
				gbc_lbln.gridy = 0;
				panel.add(lbln, gbc_lbln);
			}
			
			JLabel lblNewLabel_3 = new JLabel("<html>\u0413\u043E\u0440\u044F\u0447\u0438\u0435 \u043A\u043D\u043E\u043F\u043A\u0438<br>\u041B\u0435\u0432\u043E&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\u041F\u0440\u0430\u0432\u043E</html>");
			lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
			
			GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
			gbc_lblNewLabel_3.anchor = GridBagConstraints.NORTH;
			gbc_lblNewLabel_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_3.gridx = 5;
			gbc_lblNewLabel_3.gridy = 0;
			gbc_lblNewLabel_3.gridwidth= 2;
			panel.add(lblNewLabel_3, gbc_lblNewLabel_3);
			{
				JLabel lblNewLabel_4 = new JLabel("\u0421\u043A\u043E\u0440\u043E\u0441\u0442\u044C");
				lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
				gbc_lblNewLabel_4.anchor = GridBagConstraints.NORTH;
				gbc_lblNewLabel_4.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
				gbc_lblNewLabel_4.gridx = 7;
				gbc_lblNewLabel_4.gridy = 0;
				panel.add(lblNewLabel_4, gbc_lblNewLabel_4);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 1");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 1;
				panel.add(label, gbc_label);
			}
			{					
				JTextField textField = new JTextField();
				textField.setName("motor_1");
				textField.setText((String) Configuration.getParametr("motor_1", false));
				textField.addFocusListener(motorNameAdapter);				
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 1;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_1");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_1", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 1;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("sensor_1");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 1;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_1");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 1;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();				
				textField.setName("leftkey_1");
				textField.setText((String)Configuration.getParametr("leftkey_1", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 1;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
				
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_1");				
				textField.setText((String)Configuration.getParametr("rightkey_1", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 1;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_1");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 1;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 2");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 2;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_2");
				textField.setText((String) Configuration.getParametr("motor_2", false));
				textField.addFocusListener(motorNameAdapter);				
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 2;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_2");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_2", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 2;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("sensor_2");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 2;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_2");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 2;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_2");
				textField.setText((String)Configuration.getParametr("leftkey_2", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 2;
				panel.add(textField, gbc_textField);			
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_2");
				textField.setText((String)Configuration.getParametr("rightkey_2", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 2;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_2");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 2;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 3");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 3;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_3");
				textField.setText((String) Configuration.getParametr("motor_3", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 3;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_3");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_3", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 3;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("sensor_3");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 3;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_3");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 3;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_3");
				textField.setText((String)Configuration.getParametr("leftkey_3", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 3;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_3");
				textField.setText((String)Configuration.getParametr("rightkey_3", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 3;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_3");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 3;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 4");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 4;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_4");
				textField.setText((String) Configuration.getParametr("motor_4", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 4;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_4");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_4", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 4;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");				
				checkBox.setName("sensor_4");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 4;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_4");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 4;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_4");
				textField.setText((String)Configuration.getParametr("leftkey_4", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 4;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_4");
				textField.setText((String)Configuration.getParametr("rightkey_4", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 4;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_4");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 4;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 5");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 5;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_5");
				textField.setText((String) Configuration.getParametr("motor_5", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 5;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_5");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_5", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 5;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");				
				checkBox.setName("sensor_5");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 5;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_5");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 5;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_5");
				textField.setText((String)Configuration.getParametr("leftkey_5", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 5;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_5");
				textField.setText((String)Configuration.getParametr("rightkey_5", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 5;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_5");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 5;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 6");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 6;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_6");
				textField.setText((String) Configuration.getParametr("motor_6", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 6;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_6");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_6", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 6;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");				
				checkBox.setName("sensor_6");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 6;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_6");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 6;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_6");
				textField.setText((String)Configuration.getParametr("leftkey_6", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 6;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_6");
				textField.setText((String)Configuration.getParametr("rightkey_6", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 6;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_6");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 6;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 7");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 7;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_7");
				textField.setText((String) Configuration.getParametr("motor_7", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 7;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_7");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_7", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 7;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JCheckBox checkBox = new JCheckBox("");			
				checkBox.setName("sensor_7");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 7;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_7");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 7;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_7");
				textField.setText((String)Configuration.getParametr("leftkey_7", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 7;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_7");
				textField.setText((String)Configuration.getParametr("rightkey_7", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 7;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_7");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 7;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 8");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 8;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("motor_8");
				textField.setText((String) Configuration.getParametr("motor_8", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 8;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("motor_8");
				checkBox.setSelected((Boolean) Configuration.getParametr("motor_8", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 8;
				panel.add(checkBox, gbc_checkBox);
			}			
			{
				JCheckBox checkBox = new JCheckBox("");			
				checkBox.setName("sensor_8");
				checkBox.setSelected((Boolean) Configuration.getParametr(checkBox.getName(), true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 3;
				gbc_checkBox.gridy = 8;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField(factory);
				formattedTextField.setName("lengthmin_8");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
				gbc_formattedTextField.gridx = 4;
				gbc_formattedTextField.gridy = 8;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("leftkey_8");
				textField.setText((String)Configuration.getParametr("leftkey_8", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 5;
				gbc_textField.gridy = 8;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rightkey_8");
				textField.setText((String)Configuration.getParametr("rightkey_8", false));
				textField.addKeyListener(hotKeyAdapter);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 6;
				gbc_textField.gridy = 8;
				panel.add(textField, gbc_textField);
				textField.setColumns(10);
			}
			{
				JFormattedTextField formattedTextField = new JFormattedTextField();
				formattedTextField.setName("stepperclick_8");
				formattedTextField.setValue(Configuration.getParametr(formattedTextField.getName(), false));
				formattedTextField.addFocusListener(motorNameAdapter);
				GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
				gbc_formattedTextField.insets = new Insets(0, 0, 5, 0);
				gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_formattedTextField.gridx = 7;
				gbc_formattedTextField.gridy = 8;
				panel.add(formattedTextField, gbc_formattedTextField);
			}
			{
				JLabel label = new JLabel("\u0420\u0435\u043B\u0435 1");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 9;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rele_1");
				textField.setText((String) Configuration.getParametr("rele_1", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 9;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("rele_1");
				checkBox.setSelected((Boolean) Configuration.getParametr("rele_1", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 9;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JLabel label = new JLabel("\u0420\u0435\u043B\u0435 2");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 10;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rele_2");
				textField.setText((String) Configuration.getParametr("rele_2", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 10;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("rele_2");
				checkBox.setSelected((Boolean) Configuration.getParametr("rele_2", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 10;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JLabel label = new JLabel("\u0420\u0435\u043B\u0435 3");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 11;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rele_3");
				textField.setText((String) Configuration.getParametr("rele_3", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 11;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("rele_3");
				checkBox.setSelected((Boolean) Configuration.getParametr("rele_3", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 5, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 11;
				panel.add(checkBox, gbc_checkBox);
			}
			{
				JLabel label = new JLabel("\u0420\u0435\u043B\u0435 4");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 0, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 12;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				textField.setName("rele_4");
				textField.setText((String) Configuration.getParametr("rele_4", false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 0, 5);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 12;
				panel.add(textField, gbc_textField);
			}
			{
				JCheckBox checkBox = new JCheckBox("");
				checkBox.setName("rele_4");
				checkBox.setSelected((Boolean) Configuration.getParametr("rele_4", true));
				checkBox.addItemListener(motorVisibleListener);
				GridBagConstraints gbc_checkBox = new GridBagConstraints();
				gbc_checkBox.insets = new Insets(0, 0, 0, 5);
				gbc_checkBox.gridx = 2;
				gbc_checkBox.gridy = 12;
				panel.add(checkBox, gbc_checkBox);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setToolTipText("");
			panel.setBorder(new LineBorder(new Color(0, 0, 0)));
			panel.setBounds(587, 36, 337, 351);
			contentPanel.add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 138, 155, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0 };
			gbl_panel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel label = new JLabel(
						"\u041D\u0430\u0437\u0432\u0430\u043D\u0438\u0435");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.anchor = GridBagConstraints.NORTH;
				gbc_label.insets = new Insets(10, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 0;
				panel.add(label, gbc_label);
			}
			{
				JLabel label = new JLabel(
						"\u041F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0430");
				label.setFont(new Font("Tahoma", Font.BOLD, 11));
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(10, 0, 5, 0);
				gbc_label.gridx = 1;
				gbc_label.gridy = 0;
				panel.add(label, gbc_label);
			}
			{
				JTextField textField = new JTextField();
				String name="key_1_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 1;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_1_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 1;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_2_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 2;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_2_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 2;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_3_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 3;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_3_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 3;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_4_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 4;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_4_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 4;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_5_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 5;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_5_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 5;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_6_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 6;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_6_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 6;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_7_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 7;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_7_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 7;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_8_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 8;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_8_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 8;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_9_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 9;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_9_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 9;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_10_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 10;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_10_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 10;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_11_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 11;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_11_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 11;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_12_name";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 5, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 12;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_12_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 0, 5, 10);
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 12;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				textField.setEditable(false);
				String name="key_13_name";
				textField.setName(name);
				textField.setText("\u0421\u0442\u043E\u043F");
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.insets = new Insets(0, 10, 10, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 13;
				panel.add(textField, gbc_textField);
			}
			{
				JTextField textField = new JTextField();
				String name="key_13_prog";
				textField.setName(name);
				textField.setText((String) Configuration.getParametr(name, false));
				textField.addFocusListener(motorNameAdapter);
				textField.setColumns(10);
				GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.insets = new Insets(0, 0, 10, 10);
				gbc_textField.fill = GridBagConstraints.HORIZONTAL;
				gbc_textField.gridx = 1;
				gbc_textField.gridy = 13;
				panel.add(textField, gbc_textField);
			}
		}
		{
			JLabel label = new JLabel(
					"\u041A\u043D\u043E\u043F\u043A\u0438 \u0444\u0438\u043A\u0441\u0438\u0440\u043E\u0432\u0430\u043D\u043D\u044B\u0445 \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C");
			label.setFont(new Font("Tahoma", Font.BOLD, 13));
			label.setBounds(587, 11, 235, 14);
			contentPanel.add(label);
		}
		{
			JCheckBox checkBox = new JCheckBox(
					"\u0420\u0430\u0437\u0440\u0435\u0448\u0438\u0442\u044C \u0440\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435");
			checkBox.setSelected(Configuration.current.canEdit);
			checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Configuration.current.canEdit = ((JCheckBox)e.getSource()).isSelected();
				}
			});
			checkBox.setFont(new Font("Tahoma", Font.BOLD, 11));
			checkBox.setBounds(6, 447, 193, 23);
			contentPanel.add(checkBox);
		}
		{
			JLabel lblNewLabel = new JLabel(
					"\u0420\u0430\u0431\u043E\u0447\u0438\u0439 \u043A\u0430\u0442\u0430\u043B\u043E\u0433");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel.setBounds(464, 450, 113, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			textField_dir = new JTextField();
			textField_dir.setBounds(587, 448, 315, 20);
			textField_dir.setText(Configuration.current.work_dir);
			textField_dir.setEditable(false);
			contentPanel.add(textField_dir);
			textField_dir.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();					
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Рабочий каталог");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					//
					// disable the "All files" option.
					//
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(ProgramSettingsDialog.this) == JFileChooser.APPROVE_OPTION) {
						String dir = chooser.getSelectedFile().toString();
						textField_dir.setText(dir);
						Configuration.current.work_dir = dir;
					}
				}
			});
			button.setBounds(902, 447, 22, 23);
			contentPanel.add(button);
		}
		
		JLabel lblNewLabel_1 = new JLabel("\u0427\u0430\u0441\u0442\u043E\u0442\u0430 \u0442\u0430\u0439\u043C\u0435\u0440\u0430 (\u043C\u043A\u0441)");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(10, 404, 152, 14);
		contentPanel.add(lblNewLabel_1);
		{
			JTextField textField = new JTextField();
			textField.setName("freq");
			textField.setBounds(160, 401, 51, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
			textField.addFocusListener(motorNameAdapter);
			textField.setText(Integer.toString(Configuration.current.freq));
		}
		{
			final JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Configuration.current.programType = comboBox.getSelectedIndex();
				}
			});
			comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"\u041A\u043E\u043E\u0440\u0434\u0438\u043D\u0430\u0442\u044B", "\u0428\u0430\u0433\u0438"}));
			comboBox.setBounds(364, 401, 99, 20);
			comboBox.setSelectedIndex(Configuration.current.programType);
			//Отключаем режим координат
			comboBox.setVisible(false);
			Configuration.current.programType = 1;
			contentPanel.add(comboBox);
			
			JButton button = new JButton(
					"\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0430 COM");
			button.setBounds(587, 400, 143, 23);
			contentPanel.add(button);			
			
			JLabel label = new JLabel("\u0420\u0435\u0436\u0438\u043C \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u044B");
			label.setFont(new Font("Tahoma", Font.BOLD, 11));
			label.setBounds(245, 404, 113, 14);
			//Отключаем режим координат
			label.setVisible(false);
			contentPanel.add(label);
			{
				JLabel lblNewLabel_5 = new JLabel("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438 \u043C\u043E\u0442\u043E\u0440\u043E\u0432 \u0438 \u0440\u0435\u043B\u0435");
				lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 13));
				lblNewLabel_5.setBounds(10, 11, 220, 14);
				contentPanel.add(lblNewLabel_5);
			}
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					SerialPortSettings com = SerialPortDialog.show(Configuration.current.serialPortSettings);
					if(com!=null){
						Configuration.current.serialPortSettings = com;
						SerialPortHelper.getInstance().initPort();
					}
				}
			});
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Configuration.saveConfig();
						GlobalScreen.removeNativeKeyListener(nativeKeyListener);
						isOpen = false;
						ProgramSettingsDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Configuration.init();
						GlobalScreen.removeNativeKeyListener(nativeKeyListener);
						isOpen = false;
						ProgramSettingsDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
