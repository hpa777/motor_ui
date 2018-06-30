package Core;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.KeyStroke;




import java.awt.Dimension;

import javax.swing.JTabbedPane;

import Configuration.Configuration;
import Configuration.HotKeyDialog;
import Configuration.ProgramSettingsDialog;
import Core.OpticState;
import Core.Program;
import Core.ProgramStep;
import Core.SerialPortHelper;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.BoxLayout;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;

import com.jgoodies.forms.factories.FormFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.KeyEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class MainWindow implements NativeKeyListener {

	private JFrame frmRepunsator;

	/**
	 * Launch the application.
	 * 
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmRepunsator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws FileNotFoundException
	 */
	public MainWindow() throws FileNotFoundException {
		initialize();
		refreshConnectStatus();
		refreshPrgButtons();
		refreshButtonsEnabled();

		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage()
				.getName());
		logger.setLevel(Level.ALL);
		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);

		registerKeyListner();
		// File file = new File("error.txt");
		// FileOutputStream fos = new FileOutputStream(file);
		// PrintStream err = new PrintStream(fos);
		// System.setErr(err);

	}

	/**
	 * Initialize the contents of the frame.
	 */

	public static boolean cyrcleflag = false;

	private Program program, userProg;

	private File programFile;

	private JTabbedPane tabbedPanel;

	private JButton btnAdd, btnNew, btnDel, btnRefresh, btnDecr, btnIncr,
			btnSave;

	private JMenuItem newMenuItem, addMenuItem, delMenuItem, refreshMenuItem,
			saveMenuItem, saveAsMenuItem;

	private JLabel connectStatusLabel;

	private JPanel prgPanel;

	private void registerKeyListner() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(this);
	}
	
	
	
	
	@SuppressWarnings("static-access")
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (program == null || program.programSteps.isEmpty() || program.busy || ProgramSettingsDialog.isOpen || HotKeyDialog.isOpen) {
			return;
		}		
		int keyCode = e.getKeyCode();
		if (Configuration.isHotKey(keyCode)) {
			return;
		}
		if (keyCode == Configuration.current.motorToLeft) {			
			ProgramStep step = program.programSteps.get(tabbedPanel
					.getSelectedIndex());
			step.keyPressed("L");
		} else if (keyCode == Configuration.current.motorToRight) {			
			ProgramStep step = program.programSteps.get(tabbedPanel
					.getSelectedIndex());
			step.keyPressed("R");
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (program == null || program.programSteps.isEmpty() || program.busy || ProgramSettingsDialog.isOpen || HotKeyDialog.isOpen) {
			return;
		}
		int keyCode = e.getKeyCode();
		if (keyCode == Configuration.current.startProg) {
			if (userProg != null) {
				userProg.stopProgram();
				runUserProgram("key_13_prog");
				userProg = null;
			} else if (program != null) {
				if (Program.busy) {
					program.stopProgram();
					runUserProgram("key_13_prog");
					userProg = null;
				} else {
					program.runProgram(cyrcleflag);
				}
			}

		} else if (keyCode == Configuration.current.runBlock) {
			if (program == null || program.programSteps.isEmpty())
				return;
			ProgramStep step = program.programSteps.get(tabbedPanel
					.getSelectedIndex());
			step.updateModel(false);
			try {
				step.runStep();
			} catch (InterruptedException e1) {				
				e1.printStackTrace();
			}
		} else if (keyCode == Configuration.current.nextBlock) {
			int i = tabbedPanel.getSelectedIndex();
			int c = tabbedPanel.getTabCount();
			int n = i == c - 1 ? 0 : i + 1;
			tabbedPanel.setSelectedIndex(n);
		} else if (keyCode == Configuration.current.prevBlock) {
			int i = tabbedPanel.getSelectedIndex();
			int c = tabbedPanel.getTabCount();
			int n = i == 0 ? c - 1 : i - 1;
			tabbedPanel.setSelectedIndex(n);
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void refreshButtonsEnabled() {
		boolean param1 = program != null;
		boolean param2 = tabbedPanel.getTabCount() > 0;

		btnAdd.setEnabled(param1);
		btnDel.setEnabled(param1 && param2);
		btnRefresh.setEnabled(param1 && param2);
		btnDecr.setEnabled(param1 && param2);
		btnIncr.setEnabled(param1 && param2);
		btnSave.setEnabled(param1 && param2);

		saveMenuItem.setEnabled(param1 && param2);
		saveAsMenuItem.setEnabled(param1 && param2);

		addMenuItem.setEnabled(param1);
		delMenuItem.setEnabled(param1 && param2);
		refreshMenuItem.setEnabled(param1 && param2);
	}

	private void refreshPrgButtons() {
		for (Component button : prgPanel.getComponents()) {
			((JButton) button).setText((String) Configuration.getParametr(
					button.getName() + "_name", false));
		}
	}

	private void refreshTabsName() {
		for (int i = 0; i < tabbedPanel.getTabCount(); i++) {
			tabbedPanel.setTitleAt(i, String.format("блок %s", i + 1));
		}
	}

	private void refreshTabsToolTip() {
		program.updateModels(false);
		for (int i = 0; i < tabbedPanel.getTabCount(); i++) {
			String toolTip = program.programSteps.get(i).description;
			tabbedPanel.setToolTipTextAt(i, toolTip);
		}
	}

	public static Icon getIcon(String name) {
		URL imgURL = MainWindow.class.getResource(name);
		return new ImageIcon(imgURL);
	}

	private void refreshConnectStatus() {
		connectStatusLabel
				.setIcon(SerialPortHelper.getInstance().IsPortOpen() ? getIcon("connect.png")
						: getIcon("disconnect.png"));
	}

	private void setProgramFile(File file) {
		if (file != null) {
			programFile = file;
			frmRepunsator.setTitle(String.format("Repunsator [%s]",
					programFile.getName()));
		}
	}

	private void saveAsDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "*.pr1";
			}

			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				if (f.isDirectory()) {
					return true;
				} else {
					String filename = f.getName().toLowerCase();
					return filename.endsWith(".pr1");
				}
			}
		});
		chooser.setDialogTitle("Сохранить как");
		chooser.setCurrentDirectory(new File(Configuration.getWorkDir()));
		if (chooser.showSaveDialog(frmRepunsator) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".pr1")) {
				file = new File(file.toString() + ".pr1");
			}
			setProgramFile(file);
		}
	}

	public static JLabel programStatusLabel;

	private void runUserProgram(String query) {
		String filename = (String) Configuration.getParametr(query, false);
		if (filename.isEmpty()) {
			return;
		}
		if (OpticState.breakSignal) {
			JOptionPane.showMessageDialog(frmRepunsator, "Авария!!!",
					"Внимание", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String absoluteFilePath = Configuration.current.work_dir
				+ File.separator + filename;
		File file = new File(absoluteFilePath);
		XStream xStream = new XStream(new DomDriver());
		xStream.processAnnotations(Program.class);
		try {
			userProg = (Program) xStream.fromXML(file);
			userProg.updateViews();
			userProg.runProgram(false);
		} catch (StreamException e2) {
			JOptionPane.showMessageDialog(frmRepunsator, e2.getMessage(),
					"Внимание", JOptionPane.WARNING_MESSAGE);
		}
		xStream = null;
		file = null;
		if (OpticState.breakSignal) {
			OpticState.waitBreakOff();
			JOptionPane.showMessageDialog(frmRepunsator, "Авария!!!",
					"Внимание", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void Exit() {
		if (Program.needSave) {
			String ObjButtons[] = { "Сохранить", "Не сохранять", "Отмена" };
			int PromptResult = JOptionPane.showOptionDialog(frmRepunsator,
					"Сохранить изменения в программе?", "Внимание",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, ObjButtons, ObjButtons[1]);
			if (PromptResult == JOptionPane.YES_OPTION) {
				Save();
			} else if (PromptResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		SerialPortHelper.getInstance().closePort();
		System.exit(0);
	}

	private void Save() {
		if (program == null)
			return;
		if (programFile != null) {
			program.Save(programFile);
		} else {
			saveAsDialog();
			program.Save(programFile);
		}
	}

	private boolean tbpBusy;

	private void initialize() {
		frmRepunsator = new JFrame();
		frmRepunsator.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Exit();
			}
		});

		frmRepunsator.setTitle("Repunsator");
		frmRepunsator.setBounds(100, 100, 1024, 500);
		frmRepunsator.setMinimumSize(new Dimension(1024, 500));
		frmRepunsator.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmRepunsator.getContentPane().setLayout(new BorderLayout(0, 0));

		tbpBusy = false;
		tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		tabbedPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "none");
		tabbedPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "none");
		tabbedPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "none");
		tabbedPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "none");
		tabbedPanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!tbpBusy && !program.programSteps.isEmpty()) {
					program.programSteps.get(tabbedPanel.getSelectedIndex())
							.runMotorOff(0);
				}
			}
		});
		frmRepunsator.getContentPane().add(tabbedPanel, BorderLayout.CENTER);

		JToolBar toolBar = new JToolBar();
		frmRepunsator.getContentPane().add(toolBar, BorderLayout.NORTH);

		ActionListener newProgramListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (program != null
						&& JOptionPane.showConfirmDialog(frmRepunsator,
								"Создать новую программу?", "Внимание",
								JOptionPane.YES_NO_OPTION) == 1) {
					return;
				}
				tbpBusy = true;
				tabbedPanel.removeAll();
				program = new Program();
				ProgramStep step = new ProgramStep();
				tabbedPanel.addTab("блок 1", step.stepPanel);
				program.programSteps.add(step);
				refreshButtonsEnabled();
				Program.needSave = false;
				tbpBusy = false;
			}
		};

		ActionListener addBlockListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = tabbedPanel.getSelectedIndex() + 1;
				ProgramStep step = new ProgramStep();
				program.programSteps.add(index, step);
				tabbedPanel.insertTab("", null, step.stepPanel, "", index);
				tabbedPanel.setSelectedComponent(step.stepPanel);
				refreshTabsName();
				refreshButtonsEnabled();
			}
		};

		ActionListener delBlockListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = tabbedPanel.getSelectedIndex();
				if (index == -1) {
					return;
				}
				if (JOptionPane.showConfirmDialog(frmRepunsator,
						"Удалить блок?", "Внимание", JOptionPane.YES_NO_OPTION) == 1) {
					return;
				}
				tabbedPanel.removeTabAt(index);
				program.programSteps.remove(index);
				refreshTabsName();
				refreshButtonsEnabled();
			}
		};

		ActionListener refreshListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshTabsToolTip();
			}
		};

		btnAdd = new JButton();
		btnAdd.setToolTipText("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u0431\u043B\u043E\u043A");
		btnAdd.setFocusable(false);
		btnAdd.setIcon(getIcon("AddTableHS.png"));
		btnAdd.addActionListener(addBlockListener);

		Component horizontalStrut_2 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_2);

		btnNew = new JButton();
		btnNew.setToolTipText("\u041D\u043E\u0432\u0430\u044F \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0430");
		btnNew.setFocusable(false);
		btnNew.setIcon(getIcon("NewDocumentHS.png"));
		btnNew.addActionListener(newProgramListener);
		toolBar.add(btnNew);

		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_1);

		btnSave = new JButton();
		btnSave.setToolTipText("Сохранить программу");
		btnSave.setFocusable(false);
		btnSave.setIcon(getIcon("saveHS.png"));
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Save();

			}
		});
		toolBar.add(btnSave);

		Component horizontalStrut_7 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_7);
		toolBar.add(btnAdd);

		Component horizontalStrut_3 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_3);

		btnDel = new JButton();
		btnDel.setToolTipText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C \u0431\u043B\u043E\u043A");
		btnDel.setFocusable(false);
		btnDel.setIcon(getIcon("DeleteHS.png"));
		btnDel.addActionListener(delBlockListener);
		toolBar.add(btnDel);

		Component horizontalStrut_4 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_4);

		btnRefresh = new JButton();
		btnRefresh
				.setToolTipText("\u041E\u0431\u043D\u043E\u0432\u0438\u0442\u044C");
		btnRefresh.setFocusable(false);
		btnRefresh.setIcon(getIcon("RefreshDocViewHS.png"));
		btnRefresh.addActionListener(refreshListener);
		toolBar.add(btnRefresh);

		Component horizontalStrut = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut);

		JButton btnTo = new JButton();
		btnTo.setToolTipText("\u0414\u0432\u0438\u0433\u0430\u0442\u044C \u043C\u043E\u0442\u043E\u0440\u044B \u0434\u043E \u0434\u0430\u0442\u0447\u0438\u043A\u043E\u0432 \u0438 \u0443\u0441\u0442\u0430\u043D\u043E\u0432\u0438\u0442\u044C \"0\"");
		btnTo.setFocusable(false);
		btnTo.setIcon(getIcon("Flag.png"));
		btnTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configuration.current.runMotorsToNull();
			}
		});
		toolBar.add(btnTo);

		Component horizontalStrut_5 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_5);

		btnDecr = new JButton();
		btnDecr.setToolTipText("\u0423\u043C\u0435\u043D\u044C\u0448\u0438\u0442\u044C \u0434\u043B\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0441\u0442\u044C \u0448\u0430\u0433\u0430 \u0432\u0441\u0435\u0445 \u0431\u043B\u043E\u043A\u043E\u0432");
		btnDecr.setFocusable(false);
		btnDecr.setIcon(getIcon("Decr.png"));
		btnDecr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				program.ChangeAllStepLength(false);
			}
		});
		toolBar.add(btnDecr);

		Component horizontalStrut_6 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_6);

		btnIncr = new JButton();
		btnIncr.setToolTipText("\u0423\u0432\u0435\u043B\u0438\u0447\u0438\u0442\u044C \u0434\u043B\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0441\u0442\u044C \u0448\u0430\u0433\u0430 \u0432\u0441\u0435\u0445 \u0431\u043B\u043E\u043A\u043E\u0432");
		btnIncr.setFocusable(false);
		btnIncr.setIcon(getIcon("Incr.png"));
		btnIncr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				program.ChangeAllStepLength(true);
			}
		});
		toolBar.add(btnIncr);

		JPanel footerPanel = new JPanel();
		frmRepunsator.getContentPane().add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		footerPanel.add(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("373px"), FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("373px"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("23px"), }));

		JButton button = new JButton("\u0421\u0442\u0430\u0440\u0442");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (program != null)
					program.runProgram(cyrcleflag);
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 11));
		button.setFocusable(false);
		panel.add(button, "2, 2");		

		JButton button_1 = new JButton("\u0421\u0442\u043E\u043F");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userProg != null) {
					userProg.stopProgram();
				}
				if (program != null) {
					program.stopProgram();
				}
				runUserProgram("key_13_prog");
				userProg = null;
			}
		});
		button_1.setFocusable(false);
		button_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(button_1, "4, 2");

		JCheckBox checkBox = new JCheckBox(
				"\u0417\u0430\u0446\u0438\u043A\u043B\u0438\u0442\u044C \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0443");
		checkBox.setFocusable(false);
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cyrcleflag = ((JCheckBox) e.getSource()).isSelected();
			}
		});
		panel.add(checkBox, "6, 2");

		JPanel panel_1 = new JPanel();
		footerPanel.add(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:36px"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("138px"), },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("24px"),
						FormFactory.RELATED_GAP_ROWSPEC, }));

		connectStatusLabel = new JLabel();
		panel_1.add(connectStatusLabel, "2, 2, left, default");

		programStatusLabel = new JLabel();
		panel_1.add(programStatusLabel, "4, 2");

		prgPanel = new JPanel();
		frmRepunsator.getContentPane().add(prgPanel, BorderLayout.EAST);
		prgPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("80dlu"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("80dlu"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				RowSpec.decode("13dlu"), RowSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("25dlu"), }));

		ActionListener runUserProgListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Program.busy) {
					return;
				}
				String query = String.format("%s_prog",
						((Component) e.getSource()).getName());
				runUserProgram(query);
			}
		};

		JButton prgButton = new JButton();
		prgButton.setFocusable(false);
		prgButton.setName("key_1");
		prgButton.addActionListener(runUserProgListener);
		prgPanel.add(prgButton, "2, 2, fill, fill");

		JButton prgButton_1 = new JButton();
		prgButton_1.setFocusable(false);
		prgButton_1.setName("key_2");
		prgButton_1.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_1, "4, 2, fill, fill");

		JButton prgButton_2 = new JButton();
		prgButton_2.setFocusable(false);
		prgButton_2.setName("key_3");
		prgButton_2.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_2, "2, 4, fill, fill");

		JButton prgButton_3 = new JButton();
		prgButton_3.setFocusable(false);
		prgButton_3.setName("key_4");
		prgButton_3.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_3, "4, 4, fill, fill");

		JButton prgButton_4 = new JButton();
		prgButton_4.setFocusable(false);
		prgButton_4.setName("key_5");
		prgButton_4.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_4, "2, 6, fill, fill");

		JButton prgButton_5 = new JButton();
		prgButton_5.setFocusable(false);
		prgButton_5.setName("key_6");
		prgButton_5.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_5, "4, 6, fill, fill");

		JButton prgButton_6 = new JButton();
		prgButton_6.setFocusable(false);
		prgButton_6.setName("key_7");
		prgButton_6.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_6, "2, 8, fill, fill");

		JButton prgButton_7 = new JButton();
		prgButton_7.setFocusable(false);
		prgButton_7.setName("key_8");
		prgButton_7.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_7, "4, 8, fill, fill");

		JButton prgButton_8 = new JButton();
		prgButton_8.setFocusable(false);
		prgButton_8.setName("key_9");
		prgButton_8.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_8, "2, 10, fill, fill");

		JButton prgButton_9 = new JButton();
		prgButton_9.setFocusable(false);
		prgButton_9.setName("key_10");
		prgButton_9.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_9, "4, 10, fill, fill");

		JButton prgButton_10 = new JButton();
		prgButton_10.setFocusable(false);
		prgButton_10.setName("key_11");
		prgButton_10.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_10, "2, 12, fill, fill");

		JButton prgButton_11 = new JButton();
		prgButton_11.setFocusable(false);
		prgButton_11.setName("key_12");
		prgButton_11.addActionListener(runUserProgListener);
		prgPanel.add(prgButton_11, "4, 12, fill, fill");

		JMenuBar menuBar = new JMenuBar();
		frmRepunsator.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("\u0424\u0430\u0439\u043B");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		newMenuItem = new JMenuItem(
				"\u041D\u043E\u0432\u0430\u044F \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0430");
		fileMenu.add(newMenuItem);
		newMenuItem.setIcon(getIcon("NewDocumentHS.png"));

		JMenuItem openMenuItem = new JMenuItem(
				"\u041E\u0442\u043A\u0440\u044B\u0442\u044C");
		openMenuItem.setMnemonic(KeyEvent.VK_O);
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tbpBusy = true;
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return "*.pr1";
					}

					@Override
					public boolean accept(File f) {
						// TODO Auto-generated method stub
						if (f.isDirectory()) {
							return true;
						} else {
							String filename = f.getName().toLowerCase();
							return filename.endsWith(".pr1");
						}
					}
				});
				String dir = Configuration.current.work_dir == null ? "."
						: Configuration.current.work_dir;
				chooser.setCurrentDirectory(new java.io.File(dir));
				chooser.setDialogTitle("Открыть");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				/*
				 * FileFilter type3 = new ExtensionFilter("HTML files", new
				 * String[] { ".htm", ".html" }); chooser.addChoosableFileFilter
				 * chooser.setAcceptAllFileFilterUsed(false);
				 */
				if (chooser.showOpenDialog(frmRepunsator) == JFileChooser.APPROVE_OPTION) {
					if (program != null)
						program.stopProgram();
					XStream xStream = new XStream(new DomDriver());
					xStream.processAnnotations(Program.class);
					try {
						program = (Program) xStream.fromXML(chooser
								.getSelectedFile());
						program.updateViews();
						tabbedPanel.removeAll();
						for (int i = 0; i < program.programSteps.size(); i++) {
							ProgramStep step = program.programSteps.get(i);
							tabbedPanel.addTab(String.format("блок %s", i + 1),
									null, step.stepPanel, step.description);
						}
						refreshButtonsEnabled();
						setProgramFile(chooser.getSelectedFile());
						Program.needSave = false;
					} catch (Exception e1) {
						// System.out.println(e1.getMessage());
					}
					xStream = null;
				}
				tbpBusy = false;
			}
		});
		fileMenu.add(openMenuItem);

		saveMenuItem = new JMenuItem(
				"\u0421\u043E\u0445\u0440\u0430\u043D\u0438\u0442\u044C");
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Save();
			}
		});
		fileMenu.add(saveMenuItem);

		saveAsMenuItem = new JMenuItem(
				"\u0421\u043E\u0445\u0440\u0430\u043D\u0438\u0442\u044C \u043A\u0430\u043A ...");
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (program == null)
					return;
				saveAsDialog();
				program.Save(programFile);

			}
		});
		fileMenu.add(saveAsMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("\u0412\u044B\u0445\u043E\u0434");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Exit();
			}
		});
		fileMenu.add(exitMenuItem);
		newMenuItem.addActionListener(newProgramListener);

		JMenu editMenu = new JMenu(
				"\u0420\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u0442\u044C");
		menuBar.add(editMenu);

		addMenuItem = new JMenuItem(
				"\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u0431\u043B\u043E\u043A");
		addMenuItem.setIcon(getIcon("AddTableHS.png"));
		editMenu.add(addMenuItem);
		addMenuItem.addActionListener(addBlockListener);

		delMenuItem = new JMenuItem(
				"\u0423\u0434\u0430\u043B\u0438\u0442\u044C \u0431\u043B\u043E\u043A");
		delMenuItem.setIcon(getIcon("DeleteHS.png"));
		editMenu.add(delMenuItem);
		delMenuItem.addActionListener(delBlockListener);

		refreshMenuItem = new JMenuItem(
				"\u041E\u0431\u043D\u043E\u0432\u0438\u0442\u044C");
		refreshMenuItem.setIcon(getIcon("RefreshDocViewHS.png"));
		editMenu.add(refreshMenuItem);

		JMenu settingsMenu = new JMenu(
				"\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		menuBar.add(settingsMenu);

		JMenuItem settingsMenuItem = new JMenuItem(
				"\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		settingsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				ProgramSettingsDialog.show(null);
				refreshPrgButtons();
				refreshConnectStatus();
				if (program != null) {
					program.updateViews();
				}
			}
		});
		settingsMenu.add(settingsMenuItem);

		JMenuItem hotKeyMenuItem = new JMenuItem(
				"\u0413\u043E\u0440\u044F\u0447\u0438\u0435 \u043A\u043B\u0430\u0432\u0438\u0448\u0438");
		hotKeyMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				HotKeyDialog.show(null);				
			}
		});
		settingsMenu.add(hotKeyMenuItem);

		JMenu menu = new JMenu("\u0421\u043F\u0440\u0430\u0432\u043A\u0430");
		menuBar.add(menu);

		JMenuItem menuItem_1 = new JMenuItem(
				"\u041E \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0435");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutWindow.show(null);
			}
		});
		menu.add(menuItem_1);
		refreshMenuItem.addActionListener(refreshListener);

	}
	/*
	 * public class ExtensionFilter extends FileFilter { private String
	 * extensions[];
	 * 
	 * private String description;
	 * 
	 * public ExtensionFilter(String description, String extension) {
	 * this(description, new String[] { extension }); }
	 * 
	 * public ExtensionFilter(String description, String extensions[]) {
	 * this.description = description; this.extensions = (String[])
	 * extensions.clone(); }
	 * 
	 * public boolean accept(File file) { if (file.isDirectory()) { return true;
	 * } int count = extensions.length; String path = file.getAbsolutePath();
	 * for (int i = 0; i < count; i++) { String ext = extensions[i]; if
	 * (path.endsWith(ext) && (path.charAt(path.length() - ext.length()) ==
	 * '.')) { return true; } } return false; }
	 * 
	 * public String getDescription() { return (description == null ?
	 * extensions[0] : description); } }
	 */

}
