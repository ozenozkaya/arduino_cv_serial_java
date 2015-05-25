# arduino_cv_serial_java

This application is built for communicating Arduino via serial line. But it can be used as a generic java serial terminal.
This application uses rxtx serial libraries for serial communication.

# Explanation of the code

SerialComm.java class implements a SerialPortEventListener as yıu can see below.

class SerialComm implements SerialPortEventListener 

Because SerialComm will use some of the properties of the SerialTerminal.java class, an instance of a SerialTerminal class can be registered during the contruction of SerialComm class.

public SerialComm(SerialTerminal newSerialTerm) {
		this.serialTermApp = newSerialTerm;
	}
	
The connect function will get the serial port identifier from the port name. It will take the baudrate from the combobox in the GUI and it will set the serial parameters as 8,N,1. Then it will connect by registering the related event listeners.

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
	
	
	Serial event will handle any messages. Received message will be read byte by byte and resulting message will be keeped after receiving a new line feed ('\n'). Then the incoming message will be padded to the serial receive terminal.
	
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
