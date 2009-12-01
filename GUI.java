/**
 * Contains the main window for the application
 * @author - Jonathan Nanney
 **/

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.Box;

public class GUI
{
   //The number of rows in the file list text area
   private static final int TEXTAREA_ROWS = 10;
   //The number of columns in the file list text area
   private static final int TEXTAREA_COLS = 10;
   
   private static final int WINDOW_HEIGHT = 250;
   private static final int WINDOW_WIDTH = 500;
   private RSABaseKey key;
   
   /*
    * Creates and displays a new GUI object
    **/
   public GUI() 
   {
      JFrame frame = new JFrame("File Encryption With OpenPGP");
      frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
      final Container contentPane = frame.getContentPane();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      createGUI(contentPane);
      frame.pack();
      frame.setVisible(true);
   }

   public static void main(String[] args) 
   {
      GUI gui = new GUI();
   }
   

   private void createGUI(final Container pane) 
   {
      JPanel firstPanel = createEncryptionPanel(pane);
      JPanel secondPanel = createDecryptionPanel(pane);
      JPanel thirdPanel = createRSAOptionPanel(pane);

      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.add("Encrypt Files", firstPanel);
      tabbedPane.add("Decrypt Files", secondPanel);
      tabbedPane.add("RSA Options", thirdPanel);
      pane.add(tabbedPane);

   }

   private JPanel createDecryptionPanel(final Container pane)
   {
      JButton addFileButton = new JButton("Add File");  
      JButton decryptButton = new JButton("Decrypt Files");
      JButton removeFileButton = new JButton("Remove File(s)");
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(addFileButton);
      buttonPanel.add(removeFileButton);
      buttonPanel.add(decryptButton);
      final JPanel panel = new JPanel(); 
      panel.setLayout(new BorderLayout());

      final DefaultListModel model = new DefaultListModel();
      final JList list = new JList(model);
      JScrollPane scrollingList = new JScrollPane(list);
      panel.add(scrollingList, BorderLayout.PAGE_START);
      panel.add(buttonPanel, BorderLayout.PAGE_END);

      addFileButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(pane);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = chooser.getSelectedFile();
               model.add(model.getSize(), selectedFile);
            }
         }
      });

      decryptButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            final RSAPrivateKey privateKey = (RSAPrivateKey) key;
            for(int i = 0; i < model.size(); i++)
            {
               final File current = (File) model.get(i);
               JOptionPane newFilenamePrompt = new JOptionPane();
               final String newFilename = newFilenamePrompt.showInputDialog(pane, 
                  "Type a new filename for " + current.getName());
               SwingWorker worker = new SwingWorker<Void, Void>() {
                  public Void doInBackground()
                  {
                     try
                     {
                        FileDecryptor decryptor = new FileDecryptor(current, privateKey);
                        decryptor.write(new File(current.getParentFile() + "/" + 
                           newFilename));
                     }
                     catch(MalformedPacketException mpe)
                     {
                        JOptionPane.showMessageDialog(panel, 
                           current.getName() + " is not a valid encrypted file", 
                           "Invalid file", JOptionPane.ERROR_MESSAGE);
                     }
                     catch(IOException ioe)
                     {
                        JOptionPane.showMessageDialog(null, "There was a problem "
                           + "reading or writing a file", "IO Exception", 
                           JOptionPane.ERROR_MESSAGE);
                     }
                     return null;
                  }
               };
               worker.execute();
            }
         }
      });
      return panel;
   }

   private JPanel createEncryptionPanel(final Container pane)
   {
      JPanel buttonPanel = new JPanel();
      JButton addFileButton = new JButton("Add File");  
      JButton encryptButton = new JButton("Encrypt Files");
      JButton removeFileButton = new JButton("Remove File(s)");
      buttonPanel.add(addFileButton);
      buttonPanel.add(removeFileButton);
      buttonPanel.add(encryptButton);

      final JPanel panel = new JPanel(); 
      panel.setLayout(new BorderLayout());
      final DefaultListModel model = new DefaultListModel();
      final JList list = new JList(model);
      JScrollPane scrollingList = new JScrollPane(list);
      panel.add(scrollingList, BorderLayout.PAGE_START);

      panel.add(buttonPanel, BorderLayout.PAGE_END);
      addFileButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(pane);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = chooser.getSelectedFile();
               model.add(model.getSize(), selectedFile);
            }
         }
      });

      removeFileButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            Object[] files = list.getSelectedValues();
            for(Object fileToRemove : files)
            {
               model.removeElement(fileToRemove);
            }
         }
      });

      encryptButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            if(key == null)
            {
               JOptionPane.showMessageDialog(null, "Cannot encrypt without " +
                  "a key", "Key Problem", JOptionPane.ERROR_MESSAGE);
               return;
            }

            for(int i = 0; i < model.size(); i++)
            {
               final File current = (File) model.get(i);
               JOptionPane newFilenamePrompt = new JOptionPane();
               final String newName = newFilenamePrompt.showInputDialog(pane, 
                  "Type a new filename for " + current.getName());
               SwingWorker worker = new SwingWorker<Void, Void>() {
                  public Void doInBackground() 
                  {
                     try
                     {
                        FileEncryptor encryptor = new FileEncryptor(current, 
                                                                    key);
                        encryptor.write(new File(current.getParentFile() + 
                                        "/" + newName));
                     }
                     catch(FileNotFoundException fnfe)
                     {
                        JOptionPane.showMessageDialog(null, current.getName() + 
                           " was not found", "File Not Found", 
                           JOptionPane.ERROR_MESSAGE);
                     }
                     catch(IOException ioe)
                     {
                        JOptionPane.showMessageDialog(null, "There was a problem "
                           + "reading or writing file: " + current.getName(), 
                           "IO Exception", JOptionPane.ERROR_MESSAGE);
                     }
                     return null;
                  }
               };
               worker.execute();
            }
         }
      });
      return panel;
   }

   private JPanel createRSAOptionPanel(final Container pane)
   {
      final JPanel panel = new JPanel(); 
      BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
      panel.setLayout(layout);
      JButton newKey = new JButton("Generate a new key");
      final JComboBox keySize = new JComboBox();
      keySize.addItem("1024");
      keySize.addItem("2048");
      keySize.addItem("4096");
      Dimension comboBoxSize = new Dimension(100, 20);
      keySize.setMaximumSize(comboBoxSize);
      JButton existingKey = new JButton("Use an existing key");
      JPanel buttonPanel = new JPanel(new BorderLayout());
      buttonPanel.add(newKey, BorderLayout.LINE_START);
      buttonPanel.add(existingKey, BorderLayout.LINE_END);
      panel.add(keySize); 
      panel.add(Box.createRigidArea(new Dimension(100, 100)));
      panel.add(buttonPanel);

      existingKey.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(pane);
            if(returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = (File) chooser.getSelectedFile();
               List<OpenPGPPacket> packets = null;
               try
               {
                  PacketReader reader = new PacketReader(selectedFile);
                  packets = reader.readPackets();
                  key = (RSABaseKey) (packets.get(0).getPacket());
               }
               catch(MalformedPacketException mpe)
               {
                  JOptionPane.showMessageDialog(panel, "Invalid key file", 
                  "Malformed Packet", JOptionPane.ERROR_MESSAGE);
               }
               catch(IOException ioe)
               {
                  JOptionPane.showMessageDialog(panel, "Problem reading key",
                     "IO Exception", JOptionPane.ERROR_MESSAGE);
               }
               catch(ClassCastException cce)
               {
                  JOptionPane.showMessageDialog(panel, "Invalid key file", 
                     "Invalid Key", JOptionPane.ERROR_MESSAGE);
               }
            }
         }
      });

      newKey.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt) 
         {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
               "Public and Private Keys", "priv", "pub");
            chooser.setFileFilter(filter);
            int returnValue = chooser.showSaveDialog(pane);
            if (returnValue == JFileChooser.APPROVE_OPTION) 
            {
               String length = (String) keySize.getSelectedItem();
               key = new RSAPrivateKey(Integer.valueOf(length));
               try
               {
                  RSAPrivateKey privateKey = (RSAPrivateKey) key;
                  RSABaseKey publicKey = privateKey.getPublicKey();
                  OpenPGPPacket publicPacket = new OpenPGPPacket(
                     OpenPGP.PUBLIC_KEY_PACKET_TAG, publicKey);
                  OpenPGPPacket privatePacket = new OpenPGPPacket(
                  OpenPGP.PRIVATE_KEY_PACKET_TAG, privateKey);
                  File basicFile = chooser.getSelectedFile();
                  File privateFile = new File(basicFile.toString() + ".priv");
                  File publicFile = new File(basicFile.toString() + ".pub");
                  FileOutputStream publicOut = new FileOutputStream(publicFile);
                  FileOutputStream privateOut = new FileOutputStream(privateFile);
                  publicPacket.write(publicOut);
                  privatePacket.write(privateOut);
                  publicOut.close();
                  privateOut.close();
               }
               catch(FileNotFoundException nfe)
               {
                  JOptionPane.showMessageDialog(panel,  "File Not Found",
                  "File Not Found", JOptionPane.ERROR_MESSAGE);
               }
               catch(IOException ioe)
               {
                  JOptionPane.showMessageDialog(panel,  "Problem writing file",
                  "IO Exception", JOptionPane.ERROR_MESSAGE);
               }
            }
         }
      });
      return panel;
   }
}
