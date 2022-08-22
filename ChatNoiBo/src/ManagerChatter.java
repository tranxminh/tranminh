import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import data.Data;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;

public class ManagerChatter extends JFrame {

	private JPanel contentPane;
	private JTabbedPane jTabbedPane1;
	private JLabel lblManagerPort;
	private JTextField txtServerPort;
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerChatter frame = new ManagerChatter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	
		
		
	

	public ManagerChatter() {
		initComponents();	
	}
	
   
   

	
	private void initComponents() {
			JButton jButton1 = new JButton();
	        jScrollPane2 = new javax.swing.JScrollPane();
	        list = new javax.swing.JList<>();
	        jButton2 = new javax.swing.JButton();
	        jButton3 = new javax.swing.JButton();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel jPanel1 = new JPanel();
		contentPane.add(jPanel1, BorderLayout.NORTH);
		jPanel1.setLayout(new GridLayout(0, 3, 0, 0));
		
		lblManagerPort = new JLabel("Manager Port:");
		jPanel1.add(lblManagerPort);
		
		txtServerPort = new JTextField();
		txtServerPort.setText("9669");
		jPanel1.add(txtServerPort);
		txtServerPort.setColumns(10);
		jButton1.setText("Start server");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
		jPanel1.add(jButton1);

		list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        
        jScrollPane2.setViewportView(list);
		
		jTabbedPane1 = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(jTabbedPane1, BorderLayout.CENTER);
		
		
		JPanel jPanel2 = new JPanel();
		
		contentPane.add(jPanel2, BorderLayout.EAST);	
		JPanel file = new JPanel();
		file.setBorder(new TitledBorder(null, "File", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		file.setPreferredSize(new Dimension(200, 400));
		jScrollPane2.setPreferredSize(new Dimension(180,100));
		file.add(jScrollPane2,BorderLayout.NORTH);	
		file.add(jButton2,BorderLayout.SOUTH);
		file.add(jButton3,BorderLayout.SOUTH);
		
		jPanel2.add(file);
		
		
		
		

        jButton2.setText("Open");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        this.setSize(600, 300);
		
        
       
        
	}
	private ServerSocket server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DefaultListModel mod = new DefaultListModel();
    private BufferedReader bufferedReader;
	    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
	    	 list.setModel(mod);
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	            	
	            	
	            	
	            	
	        			try {
	        				int serverPort = Integer.parseInt(txtServerPort.getText().trim());
	        				server = new ServerSocket(serverPort);
	                        Socket s = server.accept();
	                        in = new ObjectInputStream(s.getInputStream());
	                        Data1 data = (Data1) in.readObject();
	                        String name = data.getName();
	                        Socket staffSocket = server.accept();
                            ChatPanel chatPanel = new ChatPanel(staffSocket, "Manager","staff");
                            jTabbedPane1.add("staff", chatPanel);
    						chatPanel.updateUI();
	                        while (true) {                 
	                            try {
	                            	data = (Data1) in.readObject();
		                            mod.addElement(name);
		                            mod.addElement(data);
		        					
	        						Thread.sleep(1000); //refresh check connect from client after 1 second		
		                            }catch (Exception e2) {
		    	        				// TODO Auto-generated catch block
		    	        				e2.printStackTrace();
		    	        			}
	                        }
	        				
	        			} catch (Exception e3) {
	        				// TODO Auto-generated catch block
	        				e3.printStackTrace();
	        			}
	        	
	             }}).start();
	    }//GEN-LAST:event_jButton1ActionPerformed
	    
	   
	    
	    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
	        if (evt.getClickCount() == 2) {
	            if (!list.isSelectionEmpty()) {
	                if (SwingUtilities.isLeftMouseButton(evt)) {
	                    open();
	                } else if (SwingUtilities.isRightMouseButton(evt)) {
	                    save();
	                }

	            }
	        }
	        // TODO add your handling code here:
	    }//GEN-LAST:event_listMouseClicked
	   
	    private void open() {
	        Data1 data = (Data1) mod.getElementAt(list.getSelectedIndex());
	        if (data.getStatus().equals("Image")) {
	            ShowImage obj = new ShowImage(this, true);
	            ImageIcon icon = new ImageIcon(data.getFile());
	            obj.setImage(icon);
	            obj.setVisible(true);
	        }
	        if (data.getStatus().equals("File")) {
	        	String str = new String(data.getFile());
	        		System.out.println(str);
	        }
	       
	        
	    }

	    private void save() {
	        Data1 data = (Data1) mod.getElementAt(list.getSelectedIndex());
	        JFileChooser ch = new JFileChooser();
	        int c = ch.showSaveDialog(this);
	        if (c == JFileChooser.APPROVE_OPTION) {
	            try {
	                FileOutputStream out = new FileOutputStream(ch.getSelectedFile());
	                out.write(data.getFile());
	                out.close();
	            } catch (Exception e) {
	                JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
	        if (!list.isSelectionEmpty()) {
	            save();
	        }
	        // TODO add your handling code here:
	    }//GEN-LAST:event_jButton3ActionPerformed

	    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
	        if (!list.isSelectionEmpty()) {
	            open();
	        }
	        // TODO add your handling code here:
	    }//GEN-LAST:event_jButton2ActionPerformed

		  private javax.swing.JButton jButton2;
		    private javax.swing.JButton jButton3;
		    private javax.swing.JScrollPane jScrollPane2;
		    private javax.swing.JList<String> list;
		    
	   

}