package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import data.DataFile;
import tags.Decode;
import tags.Encode;
import tags.Tags;

public class ChatFrame extends JFrame {

	/**
	 *
	 */
	// Socket
	private static String URL_DIR = System.getProperty("user.dir");
	private Socket socketChat;
	private String nameUser = "", nameGuest = "", nameFile = "";
	public boolean isStop = false, isSendFile = false, isReceiveFile = false;
	private ChatRoom chat;
	private int portServer = 0;

	// Frame
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextPane txtDisplayMessage;
	private JButton btnSendFile;
	private JLabel lblReceive;
	private ChatFrame frame = this;
	private JProgressBar progressBar;
	JButton btnSend;

	public ChatFrame(String user, String guest, Socket socket, int port) throws Exception {
		super();
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		frame = new ChatFrame(user, guest, socket, port, port);
		frame.setVisible(true);
	}

	public ChatFrame(String user, String guest, Socket socket, int port, int a) throws Exception {
		// TODO Auto-generated constructor stub
		super();
		nameUser = user;
		nameGuest = guest;
		socketChat = socket;
		this.portServer = port;
		System.out.println("user: " + user);
		System.out.println("Guest: " + guest);
		System.out.println("Port: " + port);
		System.out.println("Socket: " + socket);
		chat = new ChatRoom(socketChat, nameUser, nameGuest);
		chat.start();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					initial();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

        //TN nhận
	public void updateChat_receive(String msg) {
		appendToPane(txtDisplayMessage, "<div class='left' style='width: 40%; background-color: #f1f0f0;'>" + "    "
				+ msg + "<br>" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "</div>");
	}

        //TN gửi
	public void updateChat_send(String msg) {
		appendToPane(txtDisplayMessage,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #0084ff;'>"
						+ LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "<br>" + msg
						+ "</td> </tr>" + "</table>");
	}

        
	public void updateChat_notify(String msg) {
		appendToPane(txtDisplayMessage,
				"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
						+ "<td style='width: 59%; '></td>" + "<td style='width: 40%; background-color: #f1c40f;'>" + msg
						+ "</td> </tr>" + "</table>");
	}

	public void updateChat_send_Symbol(String msg) {
		appendToPane(txtDisplayMessage, "<table style='width: 100%;'>" + "<tr align='right'>"
				+ "<td style='width: 59%;'></td>" + "<td style='width: 40%;'>" + msg + "</td> </tr>" + "</table>");
	}

	/**
	 * Create the frame.
	 */
	public void initial() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				try {
					isStop = true;
					frame.dispose();
					chat.sendMessage(Tags.CHAT_CLOSE_TAG);
					chat.stopChat();
					System.gc();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		setResizable(false);
		setTitle("BOX CHAT");
		setBounds(100, 100, 576, 595);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 573, 67);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/VKU64.png")));
		lblNewLabel.setBounds(20, 0, 66, 67);
		panel.add(lblNewLabel);

		JLabel nameLabel = new JLabel(nameGuest);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		nameLabel.setToolTipText("");
		nameLabel.setBounds(96, 10, 129, 38);
		panel.add(nameLabel);

		JButton btnPhone = new JButton();
		btnPhone.setToolTipText("voice call");
		btnPhone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Voice call");
			}
		});

		btnPhone.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/phone48.png")));
		btnPhone.setBounds(403, 16, 32, 32);
		btnPhone.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnPhone.setContentAreaFilled(false);
		panel.add(btnPhone);

		JButton btnVideo = new JButton("");
		btnVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Video call");

			}
		});
		btnVideo.setToolTipText("video call");
		btnVideo.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/videocall48.png")));
		btnVideo.setBounds(474, 16, 32, 32);
		btnVideo.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnVideo.setContentAreaFilled(false);
		panel.add(btnVideo);

		JPanel panel_6 = new JPanel();
		txtDisplayMessage = new JTextPane();
		txtDisplayMessage.setEditable(false);
		txtDisplayMessage.setContentType("text/html");
		txtDisplayMessage.setBackground(Color.BLACK);
		txtDisplayMessage.setForeground(Color.WHITE);
		txtDisplayMessage.setFont(new Font("Courier New", Font.PLAIN, 18));
		appendToPane(txtDisplayMessage, "<div class='clear' style='background-color:white'></div>"); // set default

		panel_6.setBounds(0, 66, 562, 323);
		panel_6.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(txtDisplayMessage);
		scrollPane.setBounds(0, 0, 562, 323);
		panel_6.add(scrollPane);
		contentPane.add(panel_6);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 372, 573, 73);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + ChatFrame.class.getResource("/image/like32.png") + "'></img>";
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnNewButton_2.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/like32.png")));
		btnNewButton_2.setBounds(31, 22, 44, 41);
		btnNewButton_2.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnNewButton_2.setContentAreaFilled(false);
		panel_2.add(btnNewButton_2);

		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + ChatFrame.class.getResource("/image/love32.png") + "'></img>";
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnNewButton_4.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/love32.png")));
		btnNewButton_4.setBounds(144, 22, 44, 41);
		btnNewButton_4.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnNewButton_4.setContentAreaFilled(false);
		panel_2.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + ChatFrame.class.getResource("/image/smile32.png") + "'></img>";
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnNewButton_5.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/smile32.png")));
		btnNewButton_5.setBounds(265, 22, 44, 41);
		btnNewButton_5.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnNewButton_5.setContentAreaFilled(false);
		panel_2.add(btnNewButton_5);

		JButton btnNewButton_7 = new JButton("");
		btnNewButton_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + ChatFrame.class.getResource("/image/sad32.png") + "'></img>";
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnNewButton_7.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/sad32.png")));
		btnNewButton_7.setBounds(378, 22, 44, 41);
		btnNewButton_7.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnNewButton_7.setContentAreaFilled(false);
		panel_2.add(btnNewButton_7);

		JButton btnNewButton_8 = new JButton("");
		btnNewButton_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + ChatFrame.class.getResource("/image/angry32.png") + "'></img>";
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnNewButton_8.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/angry32.png")));
		btnNewButton_8.setBounds(495, 22, 44, 41);
		btnNewButton_8.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnNewButton_8.setContentAreaFilled(false);
		panel_2.add(btnNewButton_8);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 22, 540, 41);
		progressBar.setVisible(false);
		panel_2.add(progressBar);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 446, 562, 73);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		btnSend = new JButton();
		btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSend.setContentAreaFilled(false);
		btnSend.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/send32.png")));
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = txtMessage.getText();
				// Clear messageif
				if (msg.equals(""))
					return;
				txtMessage.setText("");
				try {
					chat.sendMessage(Encode.sendMessage(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send(msg);

			}
		});
		btnSend.setBounds(498, 5, 64, 64);
		panel_3.add(btnSend);

		btnSendFile = new JButton();
		btnSendFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					isSendFile = true;
					String path_send = (fileChooser.getSelectedFile().getAbsolutePath());
					System.out.println(path_send);
					nameFile = fileChooser.getSelectedFile().getName();
					File file = fileChooser.getSelectedFile();
					// if (isSendFile)
					try {
						chat.sendMessage(Encode.sendFile(nameFile));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					System.out.println("nameFile: " + nameFile);
					try {
						chat.sendFile(file);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSendFile.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSendFile.setContentAreaFilled(false);
		btnSendFile.setIcon(new ImageIcon(ChatFrame.class.getResource("/image/file32.png")));
		btnSendFile.setBounds(440, 10, 64, 53);
		panel_3.add(btnSendFile);

		txtMessage = new JTextField();
		txtMessage.setBounds(0, 5, 433, 58);
		panel_3.add(txtMessage);
		txtMessage.setColumns(10);
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnSend.doClick();
				}
			}
		});

		lblReceive = new JLabel("");
		lblReceive.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblReceive.setBounds(47, 529, 465, 29);
		lblReceive.setVisible(false);
		contentPane.add(lblReceive);
	}

	public class ChatRoom extends Thread {

		private Socket connect;
		private ObjectOutputStream outPeer;
		private ObjectInputStream inPeer;
		private boolean continueSendFile = true, finishReceive = false;
		private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0, sizeReceive = 0;
		private String nameFileReceive = "";
		private InputStream inFileSend;
		private DataFile dataFile;

		public ChatRoom(Socket connection, String name, String guest) throws Exception {
			connect = new Socket();
			connect = connection;
			nameGuest = guest;
			System.out.println(connect);
		}

		@Override
		public void run() {
			super.run();
			System.out.println("Chat Room start");
			OutputStream out = null;
			while (!isStop) {
				try {
					inPeer = new ObjectInputStream(connect.getInputStream());
					Object obj = inPeer.readObject();
					if (obj instanceof String) {
						String msgObj = obj.toString();
						if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
							isStop = true;
							Tags.show(frame, nameGuest + "This windows will also be closed.",
									false);
							try {
								isStop = true;
								frame.dispose();
								chat.sendMessage(Tags.CHAT_CLOSE_TAG);
								chat.stopChat();
								System.gc();
							} catch (Exception e) {
								e.printStackTrace();
							}
							connect.close();
							break;
						}
						if (Decode.checkFile(msgObj)) {
							System.out.println("Check file: " + URL_DIR + "/" + nameFileReceive);
							isReceiveFile = true;
							nameFileReceive = msgObj.substring(10, msgObj.length() - 11);
							File fileReceive = new File(URL_DIR + "/" + nameFileReceive);
							if (!fileReceive.exists()) {
								fileReceive.createNewFile();
							}
							String msg = Tags.FILE_REQ_ACK_OPEN_TAG + Integer.toBinaryString(portServer)
									+ Tags.FILE_REQ_ACK_CLOSE_TAG;
							sendMessage(msg);

						} else if (Decode.checkFeedBack(msgObj)) {
							btnSendFile.setEnabled(false);

							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										sendMessage(Tags.FILE_DATA_BEGIN_TAG);
										updateChat_notify("You are sending file: " + nameFile);
										isSendFile = false;
//										sendFile(txtMessage.getText());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						} else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
							finishReceive = false;
							lblReceive.setVisible(true);
							out = new FileOutputStream(URL_DIR + nameFileReceive);
						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
							System.out.println("Close file: " + URL_DIR + "\\" + nameFileReceive);

							updateChat_receive(
									"You receive file: " + nameFileReceive + " with size " + sizeReceive + " KB");
							sizeReceive = 0;
							out.flush();
							out.close();
							lblReceive.setVisible(false);
							System.out.println("Select file save location");

							new Thread(new Runnable() {

								@Override
								public void run() {
									System.out.println("Select file save location");
									showSaveFile();
								}
							}).start();
							finishReceive = true;
//						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG) && isFileLarge == true) {
//							updateChat_receive("File " + nameFileReceive + " too large to receive");
//							sizeReceive = 0;
//							out.flush();
//							out.close();
//							lblReceive.setVisible(false);
//							finishReceive = true;
						} else {

							String message = Decode.getMessage(msgObj);

							updateChat_receive(message);
						}
					} else if (obj instanceof DataFile) {

						DataFile data = (DataFile) obj;
						++sizeReceive;
						out.write(data.data);
					}
				} catch (Exception e) {
					File fileTemp = new File(URL_DIR + nameFileReceive);
					if (fileTemp.exists() && !finishReceive) {
						fileTemp.delete();
					}
				}
			}
		}

		private void getData(File file) throws Exception {
			File fileData = file;
			if (fileData.exists()) {
				sizeOfSend = 0;
				dataFile = new DataFile();
				sizeFile = (int) fileData.length();
				sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
						: (int) (fileData.length() / 1024) + 1;
				inFileSend = new FileInputStream(fileData);
			}
		}

		public void sendFile(File file) throws Exception {

			btnSendFile.setEnabled(false);
			getData(file);
			lblReceive.setVisible(true);
			if (sizeOfData > Tags.MAX_MSG_SIZE / 1024) {
				lblReceive.setText("File is too large...");
				inFileSend.close();
				sendMessage(Tags.FILE_DATA_CLOSE_TAG);
				btnSendFile.setEnabled(true);
				isSendFile = false;
				inFileSend.close();
				return;
			}

			progressBar.setVisible(true);
			progressBar.setValue(0);

			lblReceive.setText("Sending ...");
			do {
				System.out.println("sizeOfSend : " + sizeOfSend);
				if (continueSendFile) {
					continueSendFile = false;
//					updateChat_notify("If duoc thuc thi: " + String.valueOf(continueSendFile));
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								inFileSend.read(dataFile.data);
								sendMessage(dataFile);
								sizeOfSend++;
								if (sizeOfSend == sizeOfData - 1) {
									int size = sizeFile - sizeOfSend * 1024;
									dataFile = new DataFile(size);
								}
								progressBar.setValue(sizeOfSend * 100 / sizeOfData);
								if (sizeOfSend >= sizeOfData) {
									inFileSend.close();
									isSendFile = true;
									sendMessage(Tags.FILE_DATA_CLOSE_TAG);
									progressBar.setVisible(false);
									lblReceive.setVisible(false);
									isSendFile = false;
									btnSendFile.setEnabled(true);
									updateChat_notify("File sent complete");
									inFileSend.close();
								}
								continueSendFile = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			} while (sizeOfSend < sizeOfData);
		}

		private void showSaveFile() {
			System.out.println("Chon vi tri luu file");
			while (true) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showSaveDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "/" + nameFileReceive);
					if (!file.exists()) {
						try {
							file.createNewFile();
							Thread.sleep(1000);
							InputStream input = new FileInputStream(URL_DIR + nameFileReceive);
							OutputStream output = new FileOutputStream(file.getAbsolutePath());
							copyFileReceive(input, output, URL_DIR + nameFileReceive);
						} catch (Exception e) {
							Tags.show(frame, "Your file receive has error!!!", false);
						}
						break;
					} else {
						int resultContinue = Tags.show(frame, "File is exists. You want save file?", true);
						if (resultContinue == 0)
							continue;
						else
							break;
					}
				}
			}
		}

		// void send Message
		public synchronized void sendMessage(Object obj) throws Exception {
			outPeer = new ObjectOutputStream(connect.getOutputStream());
			// only send text
			if (obj instanceof String) {
				String message = obj.toString();
				outPeer.writeObject(message);
				outPeer.flush();
				if (isReceiveFile)
					isReceiveFile = false;
			}
			// send attach file
			else if (obj instanceof DataFile) {
				outPeer.writeObject(obj);
				outPeer.flush();
			}
		}

		public void stopChat() {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void copyFileReceive(InputStream inputStr, OutputStream outputStr, String path) throws IOException {
		byte[] buffer = new byte[1024];
		int lenght;
		while ((lenght = inputStr.read(buffer)) > 0) {
			outputStr.write(buffer, 0, lenght);
		}
		inputStr.close();
		outputStr.close();
		File fileTemp = new File(path);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}

	private void appendToPane(JTextPane tp, String msg) {
		HTMLDocument doc = (HTMLDocument) tp.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit) tp.getEditorKit();
		try {

			editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
			tp.setCaretPosition(doc.getLength());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
