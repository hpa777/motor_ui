package Configuration;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;


import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import javax.swing.JLabel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class HotKeyDialog extends JDialog  {

	private final JPanel contentPanel = new JPanel();
	
	public static boolean isOpen;
	/**
	 * Launch the application.
	 */
	public static void show(String[] args) {
		try {
			HotKeyDialog dialog = new HotKeyDialog();			
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	/**
	 * Create the dialog.
	 */
	private JTextField selectedTextbox;
	
	private NativeKeyListener nativeKeyListener;
	
	public HotKeyDialog() {
		isOpen = true;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("\u0413\u043E\u0440\u044F\u0447\u0438\u0435 \u043A\u043B\u0430\u0432\u0438\u0448\u0438");
		setType(Type.UTILITY);
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 321, 236);
		getContentPane().setLayout(new BorderLayout());		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(99dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		nativeKeyListener = new NativeKeyListener() {
			
			@Override
			public void nativeKeyPressed(NativeKeyEvent e) {
				// TODO Auto-generated method stub
				if (selectedTextbox == null) return;
				int keyCode = e.getKeyCode();						
				Configuration.setParametr(selectedTextbox.getName(), keyCode);
				selectedTextbox.setText(NativeKeyEvent.getKeyText(keyCode));
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
		KeyAdapter adapter = new KeyAdapter() {						
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub				
				e.consume();
			}
		};
		selectedTextbox = null;
		FocusAdapter focusAdapter = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				selectedTextbox = (JTextField)e.getSource();
			}
		};
		
		{
			JTextField textField_runBlock = new JTextField();
			textField_runBlock.setName("runBlock");
			textField_runBlock.addFocusListener(focusAdapter);
			textField_runBlock.addKeyListener(adapter);
			contentPanel.add(textField_runBlock, "4, 4, left, center");
			textField_runBlock.setColumns(15);		
			textField_runBlock.setText((String)Configuration.getParametr("runBlock", false));
		}
		{
			JLabel label = new JLabel("\u0421\u0442\u0430\u0440\u0442 / \u0441\u0442\u043E\u043F \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u044B");
			contentPanel.add(label, "2, 2, right, center");
		}
		{
			JLabel lblCtrl = new JLabel("\u0412\u044B\u043F\u043E\u043B\u043D\u0438\u0442\u044C \u0431\u043B\u043E\u043A");
			contentPanel.add(lblCtrl, "2, 4, right, center");
		}
		{
			JTextField textField_startProg = new JTextField();
			textField_startProg.addFocusListener(focusAdapter);
			textField_startProg.setName("startProg");
			textField_startProg.addKeyListener(adapter);
			contentPanel.add(textField_startProg, "4, 2, left, center");
			textField_startProg.setColumns(15);
			textField_startProg.setText((String)Configuration.getParametr("startProg", false));
		}
		{
			JLabel lblCtrl_1 = new JLabel("\u0421\u043B\u0435\u0434\u0443\u044E\u0449\u0438\u0439 \u0431\u043B\u043E\u043A");
			contentPanel.add(lblCtrl_1, "2, 6, right, center");
		}
		{
			JTextField textField_nextBlock = new JTextField();
			textField_nextBlock.setName("nextBlock");
			textField_nextBlock.addFocusListener(focusAdapter);
			textField_nextBlock.addKeyListener(adapter);
			contentPanel.add(textField_nextBlock, "4, 6, left, center");
			textField_nextBlock.setColumns(15);
			textField_nextBlock.setText((String)Configuration.getParametr("nextBlock", false));
		}
		{
			JLabel lblNewLabel = new JLabel("\u041F\u0440\u0435\u0434\u0438\u0434\u0443\u0449\u0438\u0439 \u0431\u043B\u043E\u043A");
			contentPanel.add(lblNewLabel, "2, 8, right, center");
		}
		{
			JTextField textField_prevBlock = new JTextField();
			textField_prevBlock.setName("prevBlock");
			textField_prevBlock.addFocusListener(focusAdapter);
			textField_prevBlock.addKeyListener(adapter);
			contentPanel.add(textField_prevBlock, "4, 8, left, center");
			textField_prevBlock.setColumns(15);
			textField_prevBlock.setText((String)Configuration.getParametr("prevBlock", false));
		}
		{
			JLabel lblNewLabel_1 = new JLabel("\u041C\u043E\u0442\u043E\u0440 \u0432\u043B\u0435\u0432\u043E");
			contentPanel.add(lblNewLabel_1, "2, 10, right, center");
		}
		{
			JTextField textField_motorToLeft = new JTextField();
			textField_motorToLeft.setName("motorToLeft");
			textField_motorToLeft.addFocusListener(focusAdapter);
			textField_motorToLeft.addKeyListener(adapter);
			contentPanel.add(textField_motorToLeft, "4, 10, left, center");
			textField_motorToLeft.setColumns(15);
			textField_motorToLeft.setText((String)Configuration.getParametr("motorToLeft", false));
		}
		{
			JLabel label = new JLabel("\u041C\u043E\u0442\u043E\u0440 \u0432\u043F\u0440\u0430\u0432\u043E");
			contentPanel.add(label, "2, 12, right, center");
		}
		{
			JTextField textField_motorToRight = new JTextField();
			textField_motorToRight.setName("motorToRight");
			textField_motorToRight.addFocusListener(focusAdapter);
			textField_motorToRight.addKeyListener(adapter);
			contentPanel.add(textField_motorToRight, "4, 12, left, center");
			textField_motorToRight.setColumns(15);
			textField_motorToRight.setText((String)Configuration.getParametr("motorToRight", false));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");				
				buttonPane.add(okButton);				
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GlobalScreen.removeNativeKeyListener(nativeKeyListener);
						Configuration.saveConfig();
						isOpen = false;
						HotKeyDialog.this.dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						GlobalScreen.removeNativeKeyListener(nativeKeyListener);
						Configuration.init();
						isOpen = false;
						HotKeyDialog.this.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	

}
