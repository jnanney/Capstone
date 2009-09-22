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
   private static final int TEXTAREA_ROWS = 10;
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
      JPanel firstPanel = createFirstPanel(pane, fileList);
      JPanel secondPanel = createSecondPanel(pane);

      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.add("Add Files", firstPanel);
      tabbedPane.add("Options", secondPanel);
      pane.add(tabbedPane);

   }
   private JPanel createSecondPanel(final Container pane)
   {
      JPanel second = new JPanel();
      return second;
   }

   private JPanel createFirstPanel(final Container pane, final List<File> 
      fileList)
   {
      JButton addFileButton = new JButton("Add File");  
      final JTextArea fileText = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
      JScrollPane scrollingFileText = new JScrollPane(fileText);
      JPanel panel = new JPanel(); 
      panel.setLayout(new BorderLayout());

      panel.add(scrollingFileText, BorderLayout.PAGE_START);
      panel.add(addFileButton, BorderLayout.LINE_START);

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
      return panel;
   }

}
