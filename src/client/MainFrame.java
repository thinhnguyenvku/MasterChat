package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import tags.Tags;

public class MainFrame extends JFrame implements WindowListener {

	private JPanel contentPane;
	private Client clientNode;
	private static String IPClient = "", nameUser = "", dataUser = "";
	private static int portClient = 0;
	private static JList<String> listActive;
	private static int portServer;
	private String name;
	static DefaultListModel<String> model = new DefaultListModel<>();
	String file = System.getProperty("user.dir") + "\\Server.txt";
	private JButton btnSaveServer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame(String arg, int arg1, String name, String msg, int port_Server) throws Exception {
		IPClient = arg;
		portClient = arg1;
		nameUser = name;
		dataUser = msg;
		portServer = port_Server;
		System.out.println("Port Server Main UI: " + portServer);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void updateFriendMainFrame(String msg) {
		model.addElement(msg);
	}

	public static void resetList() {
		model.clear();
	}

	void SaveServer() {
		try {
//			PrintWriter printWriter = new PrintWriter(new File(file));
//			StringBuilder stringBuilder = new StringBuilder();
//			stringBuilder.append(IPClient + " " + portServer);
//			printWriter.append(stringBuilder.toString());
//			printWriter.close();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(IPClient + " " + portServer);
			bw.newLine();
			bw.close();

			JOptionPane.showMessageDialog(this, "Server has been saved.");
			btnSaveServer.setVisible(false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 *
	 * @throws Exception
	 */
	public MainFrame() throws Exception {
                setTitle("VKU Client");
		this.addWindowListener(this);
		setResizable(false);

		System.out.println("Port Server Main UI: " + portServer);
		updateFriendMainFrame("12");
		clientNode = new Client(IPClient, portClient, nameUser, dataUser, portServer);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 677, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Chat Client");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		lblNewLabel.setBounds(226, 10, 255, 64);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Username: " + nameUser);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(27, 80, 309, 47);
		contentPane.add(lblNewLabel_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
				"Online Users", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(27, 164, 613, 344);

		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 1));

		listActive = new JList<>(model);
		listActive.setBorder(new EmptyBorder(5, 5, 5, 5));
		listActive.setBackground(Color.WHITE);
		listActive.setForeground(Color.RED);
		listActive.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listActive.setBounds(10, 20, 577, 332);
		JScrollPane listPane = new JScrollPane(listActive);

		panel.add(listPane);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"VKU Server", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.textHighlight));
		panel_1.setForeground(Color.BLUE);
		panel_1.setBackground(Color.BLACK);
		panel_1.setBounds(453, 10, 187, 108);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("IP Address");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(10, 10, 85, 24);
		panel_1.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Port Server");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(10, 44, 85, 13);
		panel_1.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("127.0.0.1");
		try {
			lblNewLabel_4.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_4.setForeground(Color.GREEN);
		lblNewLabel_4.setBounds(88, 9, 115, 24);
		panel_1.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel(String.valueOf(portServer));
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_5.setForeground(Color.RED);
		lblNewLabel_5.setBounds(88, 44, 74, 17);
		panel_1.add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Port Client");
		lblNewLabel_6.setForeground(Color.WHITE);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_6.setBounds(10, 81, 74, 13);
		panel_1.add(lblNewLabel_6);

		JLabel lblNewLabel_7 = new JLabel(String.valueOf(portClient));
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_7.setForeground(new Color(255, 20, 147));
		lblNewLabel_7.setBounds(88, 80, 89, 13);
		panel_1.add(lblNewLabel_7);

		btnSaveServer = new JButton("Save Server");
		btnSaveServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Save Server
				SaveServer();
			}
		});
		btnSaveServer.setFocusable(false);
		btnSaveServer.setBounds(488, 128, 112, 27);
		contentPane.add(btnSaveServer);
		listActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				name = listActive.getModel().getElementAt(listActive.locationToIndex(arg0.getPoint()));
				connectChat();
			}
		});
	}

	private void connectChat() {
		// TODO Auto-generated method stub
		int n = JOptionPane.showConfirmDialog(this, "Would you like to connect to this account?", "Connect",
				JOptionPane.YES_NO_OPTION);
		if (n == 0) {
			System.out.println(name);
			if (name.equals("") || Client.clientarray == null) {
				Tags.show(this, "Invaild username", false);
				return;
			}
			if (name.equals(nameUser)) {
				Tags.show(this, "This software doesn't support chat yourself function", false);
				return;
			}
			int size = Client.clientarray.size();
			for (int i = 0; i < size; i++) {
				if (name.equals(Client.clientarray.get(i).getName())) {
					try {
						clientNode.intialNewChat(Client.clientarray.get(i).getHost(),
								Client.clientarray.get(i).getPort(), name);
						return;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			Tags.show(this, "Friend is not found. Please wait to update your list friend", false);
		}
	}

	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		return Tags.show(frameMessage, msg, type);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		// Only debug
//		Tags.show(this, "Are you sure to leave", true);
		try {
			clientNode.exit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
