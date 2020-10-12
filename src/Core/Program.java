package Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jssc.SerialPort;
import jssc.SerialPortException;
import Configuration.Configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Program {

	public List<ProgramStep> programSteps;
	
	public static boolean busy, needSave;
	
	public int stepCount;

	public Program() {
		programSteps = new ArrayList<ProgramStep>();
	}

	public byte[] updateModels(boolean commitEdit) {
		int len = this.programSteps.size();
		byte[] buffer = new byte[24 * len];
		int i = 0;
		for (ProgramStep step : programSteps) {
			byte[] buf = step.updateModel(commitEdit);
			System.arraycopy(buf, 0, buffer, i, 24);
			i+=24;
			if (step.isStop) {
				break;
			}
		}
		return buffer;
	}

	public void updateViews() {
		for (ProgramStep step : programSteps) {
			step.updateView();
		}
	}

	public void Save(File file) {
		if (file == null)
			return;
		updateModels(true);
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(file, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (printWriter != null) {
			XStream xStream = new XStream(new DomDriver());
			xStream.processAnnotations(Program.class);
			printWriter.print(xStream.toXML(Program.this));
			printWriter.close();
			needSave = false;
		}
	}	
	
	public void ChangeAllStepLength(boolean dir){
		for (ProgramStep step : this.programSteps) {
			if(!dir){
				step.DecreaseStepLength();
			} else {
				step.IncreaseStepLength();
			}
		}
	}
	
	@XStreamOmitField
	PortReader reader;
	
	public void runProgram(boolean cyrcl) {
			reader = new PortReader();
			reader.runProgram(cyrcl);			
	}
	
	public void stopProgram() {
		if(reader != null && busy) {
			reader.stopProgram();
			reader=null;
		}
	}
	
	public boolean setBreakSensor(boolean state) {
		if (reader != null) {
			return reader.setBreakSensor(state);
		}
		return false;
	}
	
	private class PortReader extends SerialPortReader {

		private Pattern blockNumber;

		public PortReader() {
			blockNumber = Pattern.compile("#(\\d*)");
		}
		
		public boolean setBreakSensor(boolean state) {
			ok=false;
			SerialPortHelper.getInstance().Write(state ? "bs_on" : "bs_off");
			synchronized (waitOk) {
				try {
					waitOk.wait(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return ok;
		}
		
		public void stopProgram(){
			ok=false;
			SerialPortHelper.getInstance().Write("stop");
			synchronized (waitOk) {
				try {
					waitOk.wait(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ok){
				busy = false;
				SerialPortHelper.getInstance().ClearEvent();
			}
		}

		public void runProgram(boolean cyrcl) {
			if (!busy) {
				MainWindow.getInstance().programStatusLabel.setIcon(MainWindow.getIcon("Play.png"));
				byte[] buff = updateModels(true);
				Integer crc = SerialPortHelper.crc16(buff);
				int len = buff.length;
				if (cyrcl) {
					len |= 1 << 31;
				}
				ok = false;
				SerialPortHelper instance = SerialPortHelper.getInstance();
				try {
					instance.serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
					instance.serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
					instance.ClearEvent();
					instance.Write(String.format("frq %s",
							Configuration.current.freq));
					instance.serialPort.addEventListener(this,
							SerialPort.MASK_RXCHAR);
					instance.serialPort.writeString("#");
					instance.serialPort.writeBytes(SerialPortHelper
							.intToByteArray(len));
					instance.serialPort.writeBytes(buff);
					instance.serialPort.writeBytes(SerialPortHelper
							.intToByteArray(crc));
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (waitOk) {
					try {						
						waitOk.wait(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ok) {
					busy = ok;
					cyrcleCount = 1;
				} else {
					MainWindow.getInstance().programStatusLabel.setText("Ошибка запуска программы");
				}
			}
		}
		
		private int cyrcleCount;

		@Override
		public void parseMessage(String answer) {
			//System.out.println(answer);
			if (answer.equals("PRG STOP") || answer.equals("PRG BREAK")) {
				busy = false;
				SerialPortHelper.getInstance().ClearEvent();
				MainWindow window = MainWindow.getInstance();
				window.programStatusLabel.setIcon(null);
				window.programStatusLabel.setText("Программа завершена" + (answer.equals("PRG BREAK") ? ". Сработал датчик обрыва." : ""));
				window.breakSensCheckBox.setSelected(false);
				window.breakSensCheckBox.setEnabled(false);
				return;
			}
			Matcher matcher = blockNumber.matcher(answer);
			if (matcher.find()) {
				int bn = Integer.parseInt(matcher.group(1), 10);
				if(bn == 0 || bn == programSteps.size()){
					cyrcleCount++;
				} else {
					MainWindow.getInstance().programStatusLabel.setText(String.format("блок %s цикл %s",
							bn + 1, cyrcleCount));
				}				
			}

		}

	}

}
