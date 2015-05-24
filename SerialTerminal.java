import gnu.io.CommPortIdentifier;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.ComponentOrientation;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.util.Enumeration;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class SerialTerminal {

	private JFrame frmSerialTerminal;
	private SerialComm serialComm = new SerialComm(this);

	JComboBox<String> comboBoxCOM = new JComboBox<String>();
	JComboBox<String> comboBoxBaud = new JComboBox<String>();
	JTextPane txtpnSerialReceive = new JTextPane();
	JTextArea textAreaSerialSend = new JTextArea();
	boolean isSerialPortOpened = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SerialTerminal window = new SerialTerminal();
					window.frmSerialTerminal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SerialTerminal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		frmSerialTerminal = new JFrame();
		frmSerialTerminal.setTitle("Serial Terminal : PACKT");
		frmSerialTerminal.setBounds(100, 100, 640, 480);
		frmSerialTerminal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSerialTerminal.getContentPane().setLayout(
				new BoxLayout(frmSerialTerminal.getContentPane(),
						BoxLayout.X_AXIS));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0,
				0, 0)));

		frmSerialTerminal.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Serial Terminal", null, panel, null);

		JPanel panelSerialConf = new JPanel();
		panelSerialConf.setBorder(new TitledBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Serial Terminal",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64,
						64)), "Serial Configuration", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelSerialConf
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JLabel lblCOM = new JLabel("COM Port:");

		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier
				.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			comboBoxCOM.addItem(portIdentifier.getName());
		}

		JLabel lblBaud = new JLabel("BaudRate:");

		comboBoxBaud.addItem("9600");

		JButton btnOpenClose = new JButton("Open");
		btnOpenClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isSerialPortOpened == false) {
					try {
						serialComm.connect(comboBoxCOM.getSelectedItem()
								.toString());
						txtpnSerialReceive.setText("");
						writeLog("Port opened", Color.GREEN);
						btnOpenClose.setText("Close");
						isSerialPortOpened = true;
					} catch (Exception e) {
						writeLog("Port open error", Color.RED);
						e.printStackTrace();
					}

				} else {
					try {
						serialComm.disconnect(comboBoxCOM.getSelectedItem()
								.toString());
						writeLog("Port closed", Color.GREEN);
						btnOpenClose.setText("Open");
						isSerialPortOpened = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						writeLog("Port close error", Color.RED);
						e.printStackTrace();
					}

				}

			}
		});
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		panel.add(panelSerialConf);

		JPanel panelSerialSend = new JPanel();
		panelSerialSend.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Serial Send",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));

		JButton btnSend = new JButton("Send Data");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					serialComm.getSerialPortWriter().write(
							textAreaSerialSend.getText().getBytes());
					writeLog("Message: \"" + textAreaSerialSend.getText()
							+ "\" sent", Color.GREEN);
				} catch (IOException e) {
					writeLog("Message: \"" + textAreaSerialSend.getText()
							+ "\" could not be sent", Color.RED);
					e.printStackTrace();
				}
			}
		});
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textAreaSerialSend.setColumns(1);
		textAreaSerialSend.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textAreaSerialSend.setText("Text to be sent");
		textAreaSerialSend.setToolTipText("Write here some text to send!");
		textAreaSerialSend.setBorder(border);
		GroupLayout gl_panelSerialSend = new GroupLayout(panelSerialSend);
		gl_panelSerialSend
				.setHorizontalGroup(gl_panelSerialSend
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_panelSerialSend
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panelSerialSend
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																textAreaSerialSend,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																559,
																Short.MAX_VALUE)
														.addComponent(
																btnSend,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																559,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_panelSerialSend.setVerticalGroup(gl_panelSerialSend
				.createParallelGroup(Alignment.TRAILING).addGroup(
						gl_panelSerialSend
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(textAreaSerialSend,
										GroupLayout.DEFAULT_SIZE, 76,
										Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnSend).addContainerGap()));
		panelSerialSend.setLayout(gl_panelSerialSend);
		GroupLayout gl_panelSerialConf = new GroupLayout(panelSerialConf);
		gl_panelSerialConf
				.setHorizontalGroup(gl_panelSerialConf
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_panelSerialConf
										.createSequentialGroup()
										.addGroup(
												gl_panelSerialConf
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																panelSerialSend,
																GroupLayout.DEFAULT_SIZE,
																591,
																Short.MAX_VALUE)
														.addGroup(
																gl_panelSerialConf
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				lblCOM)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				comboBoxCOM,
																				GroupLayout.PREFERRED_SIZE,
																				99,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(18)
																		.addComponent(
																				lblBaud)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				comboBoxBaud,
																				0,
																				143,
																				Short.MAX_VALUE)
																		.addGap(72)
																		.addComponent(
																				btnOpenClose,
																				GroupLayout.PREFERRED_SIZE,
																				140,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		gl_panelSerialConf
				.setVerticalGroup(gl_panelSerialConf
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelSerialConf
										.createSequentialGroup()
										.addGap(12)
										.addGroup(
												gl_panelSerialConf
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(lblCOM)
														.addComponent(
																comboBoxCOM,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lblBaud)
														.addComponent(
																comboBoxBaud,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																btnOpenClose))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(panelSerialSend,
												GroupLayout.PREFERRED_SIZE,
												133, GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		panelSerialConf.setLayout(gl_panelSerialConf);

		JPanel panelSerialReceive = new JPanel();
		panel.add(panelSerialReceive);
		panelSerialReceive.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Serial Receive Terminal",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));

		txtpnSerialReceive.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtpnSerialReceive.setText("Received Data");

		JScrollPane scrollPaneTerminal = new JScrollPane(txtpnSerialReceive);
		scrollPaneTerminal.setViewportBorder(new MatteBorder(1, 1, 1, 1,
				(Color) new Color(0, 0, 0)));
		panelSerialReceive.add(scrollPaneTerminal);



		GroupLayout gl_panelSerialReceive = new GroupLayout(panelSerialReceive);
		gl_panelSerialReceive.setHorizontalGroup(gl_panelSerialReceive
				.createParallelGroup(Alignment.LEADING).addGroup(
						gl_panelSerialReceive
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(scrollPaneTerminal,
										GroupLayout.DEFAULT_SIZE, 585,
										Short.MAX_VALUE).addContainerGap()));
		gl_panelSerialReceive.setVerticalGroup(gl_panelSerialReceive
				.createParallelGroup(Alignment.LEADING).addGroup(
						gl_panelSerialReceive
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(scrollPaneTerminal,
										GroupLayout.PREFERRED_SIZE, 149,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(23, Short.MAX_VALUE)));
		panelSerialReceive.setLayout(gl_panelSerialReceive);

		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		frmSerialTerminal.setVisible(true);
	}

	public int GetSerialBaudRate() {
		return Integer.parseInt(comboBoxBaud.getSelectedItem().toString());
	}

	public void AppendToSerialTerminal(String receivedStr) {
		writeLog(receivedStr, Color.BLUE);
	}

	public void writeLog(String msg, Color c) {

		if (!msg.equals("\n")) {
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
					StyleConstants.Foreground, c);

			((DefaultCaret) txtpnSerialReceive.getCaret())
					.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Tahoma");
			aset = sc.addAttribute(aset, StyleConstants.Alignment,
					StyleConstants.ALIGN_JUSTIFIED);
			try {
				msg += "\r\n";
				txtpnSerialReceive.getStyledDocument().insertString(
						txtpnSerialReceive.getStyledDocument().getLength(),
						msg, aset);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
}

