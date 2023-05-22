package server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import data.Peer;

public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtIP;
	private JTextField txtPort;
	private JLabel lblStatus;
	private static JTextArea txtMessage;
	public static JLabel lblUserOnline;
	public static int port = 3939;
	static ServerCore server;
	JButton btnStopServer, btnStartServer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void updateMessage(String msg) {
		txtMessage.append(msg + "\n");
	}

	public static void updateNumberClient() {
		int number = Integer.parseInt(lblUserOnline.getText());
		lblUserOnline.setText(Integer.toString(number + 1));
		displayUser();

	}

	public static void decreaseNumberClient() {
		int number = Integer.parseInt(lblUserOnline.getText());
		lblUserOnline.setText(Integer.toString(number - 1));
		displayUser();

	}

	static void displayUser() {
		txtMessage.setText("");
		ArrayList<Peer> list = server.getListUser();
		for (int i = 0; i < list.size(); i++) {
			txtMessage.append((i + 1) + "\t" + list.get(i).getName() + "\n");
		}
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setResizable(false);
		setTitle("VKU Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 832, 757);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("VKU SERVER");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		lblNewLabel.setBounds(300, 0, 245, 76);
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(31, 100, 279, 34);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JLabel lblNewLabel_1 = new JLabel("IP");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		panel.add(lblNewLabel_1);

		panel.add(new JPanel());
		txtIP = new JTextField();
		txtIP.setEditable(false);
		txtIP.setForeground(Color.BLUE);
		txtIP.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		panel.add(txtIP);
		txtIP.setColumns(10);
		try {
			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(31, 154, 279, 34);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("Port");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(0, 10, 55, 24);
		panel_1.add(lblNewLabel_2);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(18, 0, 10, 34);
		panel_1.add(panel_2);

		txtPort = new JTextField();
		txtPort.setForeground(Color.RED);
		txtPort.setText("3939");
		txtPort.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		txtPort.setBounds(40, 0, 185, 34);
		panel_1.add(txtPort);
		txtPort.setColumns(10);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"VKU Server", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBackground(Color.CYAN);
		panel_3.setBounds(432, 87, 357, 166);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblNewLabel_3 = new JLabel("Status");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(22, 36, 74, 17);
		panel_3.add(lblNewLabel_3);

		lblStatus = new JLabel("OFF");
		lblStatus.setForeground(Color.RED);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStatus.setBounds(223, 23, 124, 43);
		panel_3.add(lblStatus);

		JLabel lblNewLabel_5 = new JLabel("Users Number");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_5.setBounds(22, 94, 184, 32);
		panel_3.add(lblNewLabel_5);

		lblUserOnline = new JLabel("0");
		lblUserOnline.setForeground(Color.BLUE);
		lblUserOnline.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUserOnline.setBounds(223, 94, 84, 32);
		panel_3.add(lblUserOnline);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(104, 325, 621, 64);
		contentPane.add(panel_4);

		btnStartServer = new JButton("Start VKU Server");
		btnStartServer.setFocusable(false);
		btnStartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					port = Integer.valueOf(txtPort.getText());
					server = new ServerCore(port);
					ServerFrame.updateMessage("START VKU SERVER ON PORT " + port);
					lblStatus.setText("<html><font color='blue'>ON</font></html>");
					btnStopServer.setEnabled(true);
					btnStartServer.setEnabled(false);
				} catch (Exception e1) {
					ServerFrame.updateMessage("START ERROR");
					e1.printStackTrace();
				}
			}
		});
		btnStartServer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_4.add(btnStartServer);

		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5);
		panel_4.add(new JPanel());

		btnStopServer = new JButton("Stop VKU Server");
		btnStopServer.setEnabled(false);
		btnStopServer.setFocusable(false);
		btnStopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblUserOnline.setText("0");
				try {
					server.stopserver();
					ServerFrame.updateMessage("STOP VKU SERVER");
					lblStatus.setText("<html><font color='red'>OFF</font></html>");
					btnStopServer.setEnabled(false);
					btnStartServer.setEnabled(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					ServerFrame.updateMessage("STOP VKU SERVER");
					lblStatus.setText("<html><font color='red'>OFF</font></html>");
					btnStopServer.setEnabled(false);
					btnStartServer.setEnabled(true);
				}
			}
		});
		btnStopServer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_4.add(btnStopServer);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(
				new TitledBorder(null, "Users List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtMessage = new JTextArea();
		txtMessage.setBackground(Color.BLACK);
		txtMessage.setForeground(Color.WHITE);
		txtMessage.setFont(new Font("Courier New", Font.PLAIN, 18));
		panel_6.setBounds(31, 417, 758, 293);
		panel_6.setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane scrollPane = new JScrollPane(txtMessage);
		panel_6.add(scrollPane);
		contentPane.add(panel_6);
	}

}
