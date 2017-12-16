package Configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;



import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import jssc.SerialPort;
import jssc.SerialPortList;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;




import Core.SerialPortHelper;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SerialPortDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> FlowControlBox;
	private JComboBox<String> ParityBox;
	private JComboBox<Integer> StopBitsBox;
	private JComboBox<Integer> DataBitsBox;
	private JComboBox<Integer> BaudRateBox;
	private JComboBox<String> PortNameBox;

	private static SerialPortSettings sPort;

	/**
	 * Launch the application.
	 */
	public static SerialPortSettings show(SerialPortSettings port) {
		sPort = port;
		try {
			SerialPortDialog dialog = new SerialPortDialog();
			if (sPort != null) {
				dialog.init();
			}
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sPort;
	}

	private void init() {
		PortNameBox.setSelectedItem(sPort.Name);
		BaudRateBox.setSelectedItem(sPort.Baudrate);
		DataBitsBox.setSelectedItem(sPort.Databits);
		StopBitsBox.setSelectedItem(sPort.Stopbits);
		ParityBox.setSelectedIndex(sPort.Parity);
		FlowControlBox.setSelectedIndex(sPort.Flowcontrol);
	}

	/**
	 * Create the dialog.
	 */
	public SerialPortDialog() {
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setTitle("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438 \u043F\u043E\u0440\u0442\u0430 COM");
		setType(Type.UTILITY);
		setBounds(100, 100, 341, 251);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 19, 82, 184, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 14, 20, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("\u041F\u043E\u0440\u0442");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			PortNameBox = new JComboBox<String>();
			PortNameBox.setModel(new DefaultComboBoxModel<String>(
					SerialPortList.getPortNames()));
			GridBagConstraints gbc_PortNameBox = new GridBagConstraints();
			gbc_PortNameBox.insets = new Insets(0, 0, 5, 5);
			gbc_PortNameBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_PortNameBox.gridx = 2;
			gbc_PortNameBox.gridy = 1;
			contentPanel.add(PortNameBox, gbc_PortNameBox);
		}
		{
			Integer[] model = { SerialPort.BAUDRATE_110,
					SerialPort.BAUDRATE_300, SerialPort.BAUDRATE_600,
					SerialPort.BAUDRATE_1200, SerialPort.BAUDRATE_4800,
					SerialPort.BAUDRATE_9600, SerialPort.BAUDRATE_14400,
					SerialPort.BAUDRATE_19200, SerialPort.BAUDRATE_38400,
					SerialPort.BAUDRATE_57600, SerialPort.BAUDRATE_115200,
					SerialPort.BAUDRATE_128000, SerialPort.BAUDRATE_256000 };
			{
				JLabel lblNewLabel_1 = new JLabel(
						"\u0421\u043A\u043E\u0440\u043E\u0441\u0442\u044C (\u0431\u0438\u0442/\u0441)");
				GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
				gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel_1.gridx = 1;
				gbc_lblNewLabel_1.gridy = 2;
				contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
			}
			BaudRateBox = new JComboBox<Integer>();
			BaudRateBox.setModel(new DefaultComboBoxModel<Integer>(model));
			BaudRateBox.setSelectedItem(SerialPort.BAUDRATE_9600);
			GridBagConstraints gbc_BaudRateBox = new GridBagConstraints();
			gbc_BaudRateBox.insets = new Insets(0, 0, 5, 5);
			gbc_BaudRateBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_BaudRateBox.gridx = 2;
			gbc_BaudRateBox.gridy = 2;
			contentPanel.add(BaudRateBox, gbc_BaudRateBox);
		}
		{
			Integer[] model = { SerialPort.DATABITS_5, SerialPort.DATABITS_6,
					SerialPort.DATABITS_7, SerialPort.DATABITS_8 };
			{
				JLabel lblNewLabel_2 = new JLabel(
						"\u0411\u0438\u0442\u044B \u0434\u0430\u043D\u043D\u044B\u0445");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel_2.gridx = 1;
				gbc_lblNewLabel_2.gridy = 3;
				contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
			}
			DataBitsBox = new JComboBox<Integer>();
			DataBitsBox.setModel(new DefaultComboBoxModel<Integer>(model));
			DataBitsBox.setSelectedItem(SerialPort.DATABITS_8);
			GridBagConstraints gbc_DataBitsBox = new GridBagConstraints();
			gbc_DataBitsBox.insets = new Insets(0, 0, 5, 5);
			gbc_DataBitsBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_DataBitsBox.gridx = 2;
			gbc_DataBitsBox.gridy = 3;
			contentPanel.add(DataBitsBox, gbc_DataBitsBox);
		}
		{
			Integer[] model = { SerialPort.STOPBITS_1, SerialPort.STOPBITS_1_5,
					SerialPort.STOPBITS_2 };
			{
				JLabel label = new JLabel(
						"\u0421\u0442\u043E\u043F\u043E\u0432\u044B\u0435 \u0431\u0438\u0442\u044B");
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.anchor = GridBagConstraints.WEST;
				gbc_label.gridx = 1;
				gbc_label.gridy = 4;
				contentPanel.add(label, gbc_label);
			}
			StopBitsBox = new JComboBox<Integer>();
			StopBitsBox.setModel(new DefaultComboBoxModel<Integer>(model));
			StopBitsBox.setSelectedItem(SerialPort.STOPBITS_1);
			GridBagConstraints gbc_StopBitsBox = new GridBagConstraints();
			gbc_StopBitsBox.insets = new Insets(0, 0, 5, 5);
			gbc_StopBitsBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_StopBitsBox.gridx = 2;
			gbc_StopBitsBox.gridy = 4;
			contentPanel.add(StopBitsBox, gbc_StopBitsBox);
		}
		{
			String[] model = { "None", "Odd", "Even", "Mark", "Space" };

			{
				JLabel lblNewLabel_3 = new JLabel(
						"\u0427\u0451\u0442\u043D\u043E\u0441\u0442\u044C");
				GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
				gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel_3.gridx = 1;
				gbc_lblNewLabel_3.gridy = 5;
				contentPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
			}
			ParityBox = new JComboBox<String>();
			ParityBox.setModel(new DefaultComboBoxModel<String>(model));
			GridBagConstraints gbc_ParityBox = new GridBagConstraints();
			gbc_ParityBox.insets = new Insets(0, 0, 5, 5);
			gbc_ParityBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_ParityBox.gridx = 2;
			gbc_ParityBox.gridy = 5;
			contentPanel.add(ParityBox, gbc_ParityBox);
		}
		{
			String[] model = { "None", "Hardware", "Xon/Xoff" };
			{
				JLabel lblNewLabel_4 = new JLabel(
						"\u0423\u043F\u0440\u0430\u0432\u043B\u0435\u043D\u0438\u0435 \u043F\u043E\u0442\u043E\u043A\u043E\u043C");
				GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
				gbc_lblNewLabel_4.insets = new Insets(0, 0, 0, 5);
				gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel_4.gridx = 1;
				gbc_lblNewLabel_4.gridy = 6;
				contentPanel.add(lblNewLabel_4, gbc_lblNewLabel_4);
			}
			FlowControlBox = new JComboBox<String>();
			FlowControlBox.setModel(new DefaultComboBoxModel<String>(model));
			GridBagConstraints gbc_FlowControlBox = new GridBagConstraints();
			gbc_FlowControlBox.insets = new Insets(0, 0, 0, 5);
			gbc_FlowControlBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_FlowControlBox.gridx = 2;
			gbc_FlowControlBox.gridy = 6;
			contentPanel.add(FlowControlBox, gbc_FlowControlBox);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");				
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");				
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getActionCommand().equals("OK")){
			 sPort = new SerialPortSettings();
				sPort.Name = (String) PortNameBox.getSelectedItem();
				sPort.Baudrate = (Integer) BaudRateBox
						.getSelectedItem();
				sPort.Databits = (Integer) DataBitsBox
						.getSelectedItem();
				sPort.Stopbits = (Integer) StopBitsBox
						.getSelectedItem();
				sPort.Parity = ParityBox.getSelectedIndex();
				sPort.Flowcontrol = FlowControlBox.getSelectedIndex();
				SerialPortHelper.getInstance().closePort();								
		 } else {
			sPort = null;
		}
		 SerialPortDialog.this.dispose();
	}
}
