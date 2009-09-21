/**
 * Contains the main window for the application
 * Author - Jonathan Nanney
 **/

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.Popup;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.ArrayList;


public class GUI
{
   private static final int TEXTAREA_ROWS=10;
   private static final int TEXTAREA_COLS=10;

   public GUI()
   {
      createGUI();
   }

   public static void main(String[] args)
   {
      GUI gui = new GUI();
   }

   public void createGUI()
   {
      final JFrame frame = new JFrame("File Encryption With OpenPGP");
      Container contentPane = frame.getContentPane();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      final JTextArea fileText = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
      JButton addFileButton = new JButton("Add File");  
      JButton encryptButton = new JButton("Encrypt Files");
      final ArrayList<File> fileList = new ArrayList<File>();
      JScrollPane scrollingFileText = new JScrollPane(fileText);

      contentPane.add(scrollingFileText, BorderLayout.PAGE_START);
      contentPane.add(addFileButton, BorderLayout.LINE_START);
      contentPane.add(encryptButton, BorderLayout.LINE_END);

      addFileButton.addActionListener(new ActionListener() 
      {
         public void actionPerformed(ActionEvent evt)
         {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(frame);
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
            
         }
      });

      frame.pack();
      frame.setVisible(true);
   }
}
