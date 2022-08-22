import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.border.TitledBorder;

import data.Data;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.DropMode;




public class ChatPanel extends JPanel {

	Socket socket = null;
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread outPutThread = null;
	String sender, receiver;
	String text;
	JTextArea txtMessages;
	JTextArea txtMessage;
	ObjectOutputStream out;
	
	
	
	
	public JTextArea getTxtMessages() {
		return this.txtMessages;
	}
	
	public ChatPanel(Socket s, String sender, String receiver) throws ClassNotFoundException, SQLException {
		initComponents();
		
		this.socket = s;
		this.sender = sender;
		this.receiver = receiver;
		
		try {
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new DataOutputStream(socket.getOutputStream());
			outPutThread = new OutputThread(socket, txtMessages, sender, receiver);
			outPutThread.start();
			
			

			
			
		} catch (Exception e) {
			e.printStackTrace();
			     
	    }
		
		
	}
	
	
	   
	
	
		
	

	
	private void btnSendActionPerformed(ActionEvent e) throws SQLException, ClassNotFoundException {
		if(this.txtMessage.getText().trim().length() == 0) return;
		try {
			os.writeBytes(txtMessage.getText());
			os.write(13);
			os.write(10);
			os.flush();
			this.txtMessages.append(String.format("\n%s: %s", this.sender, this.txtMessage.getText()));
			
		} catch (Exception except) {
			except.printStackTrace();
		}
		Class.forName("com.mysql.cj.jdbc.Driver");
		 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat", "root", "doanvykha321");
       
		 try {
	          String sql = "Insert into chat(ChatText) "
	                  + " values (?) ";
	          // Create statement object
	          PreparedStatement stmt = con.prepareStatement(sql);
	 
	          // Set auto-commit to false
	          con.setAutoCommit(false);
	 
	          // Sét đặt các tham số.
	          stmt.setString(1, this.txtMessage.getText());
	          
	          // Thêm vào lô.
	          stmt.addBatch();
	 
	          // Sét đặt các giá trị tham số khác
	          
	      
	 
	          // Create an int[] to hold returned values
	          int[] counts = stmt.executeBatch();
	 
	          System.out.println("counts[0] = " + counts[0]);
	         
	 
	          // Explicitly commit statements to apply changes
	          con.commit();
	      } catch (Exception err) {
	          err.printStackTrace();
	          con.rollback();
	      }
	}
	
	
	
	public void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelMessage = new JPanel();
		panelMessage.setBorder(new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMessage.setToolTipText("");
		add(panelMessage, BorderLayout.SOUTH);
		
		panelMessage.setLayout(new GridLayout(1, 2, 0, 0));
		
		
		
		
		JScrollPane jScrollPane = new JScrollPane();
		panelMessage.add(jScrollPane);
		
		txtMessage = new JTextArea();
		jScrollPane.setViewportView(txtMessage);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnSendActionPerformed(e);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panelMessage.add(btnSend);
		
		
		
		
		
		
		JScrollPane jScrollPane2 = new JScrollPane();
		add(jScrollPane2, BorderLayout.CENTER);
		txtMessages = new JTextArea();
		jScrollPane2.setViewportView(txtMessages);
		
		
		
		
	}
	
	
	
	

}