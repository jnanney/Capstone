/**
 * Contains the main window for the application
 * Author - Jonathan Nanney
 **/

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.Popup;
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
   public GUI() throws Exception //TODO: replace
   {
      JFrame frame = new JFrame("File Encryption With OpenPGP");
      frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
      final Container contentPane = frame.getContentPane();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      createGUI(contentPane);
      frame.pack();
      frame.setVisible(true);
   }

   public static void main(String[] args) throws Exception //TODO: replace
   {

      GUI gui = new GUI();
   }
   

   private void createGUI(final Container pane) throws IOException, 
      FileNotFoundException
   {
      final ArrayList<File> encryptList = new ArrayList<File>();
      final ArrayList<File> decryptList = new ArrayList<File>();
      JPanel firstPanel = createEncryptionPanel(pane, encryptList);
      JPanel secondPanel = createDecryptionPanel(pane, decryptList);
      JPanel thirdPanel = createRSAOptionPanel(pane);

      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.add("Encrypt Files", firstPanel);
      tabbedPane.add("Decrypt Files", secondPanel);
      tabbedPane.add("RSA Options", thirdPanel);
      pane.add(tabbedPane);

   }

   private JPanel createDecryptionPanel(final Container pane, 
      final List<File> fileList)
   {
      JButton addFileButton = new JButton("Add File");  
      JButton decryptButton = new JButton("Decrypt Files");
      final JTextArea fileText = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
      JScrollPane scrollingFileText = new JScrollPane(fileText);
      JPanel panel = new JPanel(); 
      panel.setLayout(new BorderLayout());

      panel.add(scrollingFileText, BorderLayout.PAGE_START);
      panel.add(addFileButton, BorderLayout.LINE_START);
      panel.add(decryptButton, BorderLayout.LINE_END);
      addFileButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(pane);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = chooser.getSelectedFile();
               fileList.add(selectedFile);
               int num = fileList.size();
               fileText.append(num + ") " + selectedFile.getName() + "\n");

            }
         }
      });

      decryptButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            RSAPrivateKey privateKey = (RSAPrivateKey) key;
            for(File current : fileList)
            {
               JOptionPane newFilenamePrompt = new JOptionPane();
               String newFilename = newFilenamePrompt.showInputDialog(pane, 
                  "Type a new filename for " + current.getName());
               try
               {
                  FileDecryptor decryptor = new FileDecryptor(current, privateKey);
                  decryptor.write(new File(current.getParentFile() + "/" + 
                     newFilename));
               }
               catch(MalformedPacketException mpe)
               {
                  System.err.println(mpe.getMessage());
               }
               catch(IOException ioe)
               {
                  System.err.println(ioe.getMessage());
               }
            }
         }
      });
      return panel;
   }

   private JPanel createEncryptionPanel(final Container pane, final List<File> 
      fileList) throws IOException, FileNotFoundException
   {
      JButton addFileButton = new JButton("Add File");  
      JButton encryptButton = new JButton("Encrypt Files");
      final JTextArea fileText = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
      JScrollPane scrollingFileText = new JScrollPane(fileText);
      JPanel panel = new JPanel(); 
      panel.setLayout(new BorderLayout());

      panel.add(scrollingFileText, BorderLayout.PAGE_START);
      panel.add(addFileButton, BorderLayout.LINE_START);
      panel.add(encryptButton, BorderLayout.LINE_END);
      addFileButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(pane);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = chooser.getSelectedFile();
               fileList.add(selectedFile);
               int num = fileList.size();
               fileText.append(num + ") " + selectedFile.getName() + "\n");
            }
         }
      });

      encryptButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            for(File current : fileList)
            {
               JOptionPane newFilenamePrompt = new JOptionPane();
               String newName = newFilenamePrompt.showInputDialog(pane, 
                  "Type a new filename for " + current.getName());
               try
               {
                  FileEncryptor encryptor = new FileEncryptor(current, key);
                  encryptor.write(new File(current.getParentFile() + "/" + 
                     newName));
               }
               catch(FileNotFoundException fnfe)
               {
                  System.err.println(fnfe.getMessage());
               }
               catch(IOException ioe)
               {
                  System.err.println(ioe.getMessage());
               }
            }
         }
      });
      return panel;
   }

   private JPanel createRSAOptionPanel(final Container pane)
   {
      final JPanel panel = new JPanel();
      GridBagLayout gridbag = new GridBagLayout();
      panel.setLayout(gridbag);
      JButton newKey = new JButton("Generate a new key");
      JButton existingKey = new JButton("Use an existing key");
      final JTextField keyLength = new JTextField("1024", 10);      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.HORIZONTAL;
      panel.add(newKey, constraints);
      /*panel.add(newKey);
      panel.add(existingKey);
      panel.add(keyLength);*/
      existingKey.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(pane);
            if(returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = chooser.getSelectedFile();
               List<OpenPGPPacket> packets = null;
               try
               {
                  PacketReader reader = new PacketReader(selectedFile);
                  packets = reader.readPackets();
                  key = (RSABaseKey) (packets.get(0).getPacket());
               }
               catch(MalformedPacketException mpe)
               {
                  System.out.println(mpe.getMessage());
                  System.exit(1);
               }
               catch(IOException ioe)
               {
                  System.out.println(ioe.getMessage());
                  System.exit(1);
               }
               catch(ClassCastException cce)
               {
                  JOptionPane error = new JOptionPane("Invalid valid key file", 
                     JOptionPane.ERROR_MESSAGE);
                  error.showMessageDialog(panel, "Invalid key file");
               }
            }
         }
      });

      newKey.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent evt) 
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showSaveDialog(pane);
            if (returnValue == JFileChooser.APPROVE_OPTION) 
            {
               String length = keyLength.getText();
               key = new RSAPrivateKey(Integer.valueOf(length));
               try
               {
                  RSAPrivateKey privateKey = (RSAPrivateKey) key;
                  RSABaseKey publicKey = privateKey.getPublicKey();
                  OpenPGPPacket publicPacket = new OpenPGPPacket(
                     OpenPGP.PUBLIC_KEY_PACKET_TAG, publicKey);
                  OpenPGPPacket privatePacket = new OpenPGPPacket(
                  OpenPGP.PRIVATE_KEY_PACKET_TAG, privateKey);
                  File privateFile = chooser.getSelectedFile();
                  File publicFile = new File(privateFile.toString() + ".pub");
                  FileOutputStream publicOut = new FileOutputStream(publicFile);
                  FileOutputStream privateOut = new FileOutputStream(privateFile);
                  publicPacket.write(publicOut);
                  privatePacket.write(privateOut);
               }
               catch(FileNotFoundException nfe)
               {
                  System.out.println(nfe.getMessage());
               }
               catch(IOException ioe)
               {
                  System.out.println(ioe.getMessage());
               }
            }
         }
      });
      return panel;
   }
}

