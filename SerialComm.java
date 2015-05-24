import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */
 class SerialComm implements SerialPortEventListener {
	SerialTerminal serialTermApp;
	private SerialPort serialPort;
	private InputStream in;
	private OutputStream out;
	private String receivedMsg = new String();

	public SerialComm(SerialTerminal newSerialTerm) {
		this.serialTermApp = newSerialTerm;
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public OutputStream getSerialPortWriter() {
		return out;
	}

	public void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);

			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;

				int baudRate = serialTermApp.GetSerialBaudRate();
				int databits = 8;
				int parity = SerialPort.PARITY_NONE;

				serialPort.setSerialPortParams(baudRate, databits,
						SerialPort.STOPBITS_1, parity);
				int flowControl = SerialPort.FLOWCONTROL_NONE;

				serialPort.setFlowControlMode(flowControl);
				serialPort.setDTR(false);
				serialPort.setRTS(false);

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();

				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public void disconnect(String portName) throws Exception {
		serialPort.removeEventListener();
		serialPort.close();
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		int data;
		receivedMsg = "";
		try {
			while ((data = in.read()) > -1) {
				if (data == '\n') {
					break;
				}

				receivedMsg += (char) data;
			}
			System.out.println(receivedMsg);
			serialTermApp.AppendToSerialTerminal(receivedMsg);
			//ProcessMessage(receivedMsg);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
