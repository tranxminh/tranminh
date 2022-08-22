import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.border.TitledBorder;



import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

public class ClientChatter extends JFrame {

	private JPanel contentPane;
	private JTextField txtStaff;
	private JTextField txtServerIP;
	private JTextField txtServerPort;
	
	String mngIP = "";
	int mngPort = 0;
	String staffName = "";
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread outputThread = null;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatter frame = new ClientChatter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientChatter() {
		jComboBox1 = new javax.swing.JComboBox<>();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 746, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel jPanel1 = new JPanel();
		jPanel1.setBorder(new TitledBorder(null, "Staff and Server Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(jPanel1, BorderLayout.NORTH);
		jPanel1.setLayout(new GridLayout(1, 7, 0, 0));
		
		JLabel lblStaff = new JLabel("Staff:");
		lblStaff.setHorizontalAlignment(SwingConstants.TRAILING);
		jPanel1.add(lblStaff);
		
		txtStaff = new JTextField();
		jPanel1.add(txtStaff);
		txtStaff.setColumns(10);
		
		JLabel lblManagerIp = new JLabel("Manager IP:");
		lblManagerIp.setHorizontalAlignment(SwingConstants.TRAILING);
		jPanel1.add(lblManagerIp);
		
		txtServerIP = new JTextField();
		txtServerIP.setText("localhost");
		jPanel1.add(txtServerIP);
		txtServerIP.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setHorizontalAlignment(SwingConstants.TRAILING);
		jPanel1.add(lblPort);
		
		txtServerPort = new JTextField();
		txtServerPort.setText("9669");
		jPanel1.add(txtServerPort);
		txtServerPort.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnConnectActionPerformed(arg0);
				
			}
		});
		jPanel1.add(btnConnect);
	//
		
		JPanel jPanel2 = new JPanel();
		JButton jButton2 = new JButton();
		jButton2.setText("Send File/Image");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Image", "File" }));
        
       
		jPanel2.add(jButton2);
		jPanel2.add(jComboBox1);
		
		contentPane.add(jPanel2, BorderLayout.SOUTH);
		
	}
	private Socket socket;
	private ObjectOutputStream out;
	 
	private void btnConnectActionPerformed (ActionEvent e) {
		mngIP = this.txtServerIP.getText();
		mngPort = Integer.parseInt(this.txtServerPort.getText());
		staffName = this.txtStaff.getText();
		try {
			socket = new Socket(mngIP, mngPort);	
				 out = new ObjectOutputStream(socket.getOutputStream());
		            Data1 data = new Data1();
		            data.setStatus("new");
		            out.writeObject(data);
		            out.flush();
		    Socket mngSocket = new Socket(mngIP,mngPort);         
		            ChatPanel chatPanel = new ChatPanel(mngSocket, staffName, "Manager");
					this.getContentPane().add(chatPanel);
					chatPanel.getTxtMessages().append("Manager is running...");
					chatPanel.updateUI();
					
					bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
					os = new DataOutputStream(mngSocket.getOutputStream());
					os.writeBytes(String.format("Staff: %s", staffName));
					System.out.println(String.format("Staff: %s", staffName));
					os.write(13);
					os.write(10);
					os.flush();
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(this, "Manager is not running.");
			System.exit(0);
		}
		
		
	
	
    }//GEN-LAST:event_jButton1ActionPerformed
	
	
	
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
        	
            JFileChooser ch = new JFileChooser();
            int c = ch.showOpenDialog(this);
            if (c == JFileChooser.APPROVE_OPTION) {
                File f = ch.getSelectedFile();
                FileInputStream in = new FileInputStream(f);
                byte b[] = new byte[in.available()];
                in.read(b);
                Data1 data = new Data1();
                data.setStatus(jComboBox1.getSelectedItem() + "");
                data.setName(ch.getSelectedFile().getName());
                data.setFile(b);
                out.writeObject(data);
                out.flush();
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
	private javax.swing.JComboBox<String> jComboBox1;
	

}