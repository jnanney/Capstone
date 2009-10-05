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
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GUI
{
   //The number of rows in the file list text area
   private static final int TEXTAREA_ROWS = 10;
   //The number of columns in the file list text area
   private static final int TEXTAREA_COLS = 10;
   
   private static final int WINDOW_HEIGHT = 250;
   private static final int WINDOW_WIDTH = 500;

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
      final ArrayList<File> fileList = new ArrayList<File>();
      JPanel firstPanel = createEncryptionPanel(pane, fileList);
      JPanel secondPanel = createDecryptionPanel(pane, fileList);
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
      JPanel second = new JPanel();
      return second;
   }

   private JPanel createEncryptionPanel(final Container pane, final List<File> 
      fileList)
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
               String newFilename = newFilenamePrompt.showInputDialog(pane, 
                  "Type a new filename for " + current.getName());
               //TODO: put these files in a list and encrypt them.
            }
         }
      });
      return panel;
   }

   private JPanel createRSAOptionPanel(final Container pane)
   {
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      JButton keyLocation = new JButton("Choose a key location");
      JTextField keyLength = new JTextField("1024", 10);      
      panel.add(keyLocation, BorderLayout.LINE_END);
      panel.add(keyLength, BorderLayout.PAGE_START);

      return panel;
   }
}
