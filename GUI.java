/**
 * Contains the main window for the application
 * Author - Jonathan Nanney
 **/

//Replace the * with the packages that I'm actually using once I know what 
//I'm actually using
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

public class GUI
{
   private static final int TEXTAREA_ROWS=10;
   private static final int TEXTAREA_COLS=10;
   public static void main(String[] args)
   {
      createGUI();
   }

   public static void createGUI()
   {
      final JFrame frame = new JFrame("File Encryption With OpenPGP");
      Container contentPane = frame.getContentPane();
      final JFileChooser chooser = new JFileChooser();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      final JTextArea fileText = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
      JButton addFileButton = new JButton("Add File");  
      JButton encryptButton = new JButton("Encrypt Files");
      contentPane.add(fileText, BorderLayout.PAGE_START);
      contentPane.add(addFileButton, BorderLayout.LINE_START);
      contentPane.add(encryptButton, BorderLayout.LINE_END);

      addFileButton.addActionListener(new ActionListener() 
      {
         public void actionPerformed(ActionEvent evt)
         {
            int returnValue = chooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
               File selectedFile = chooser.getSelectedFile();
               fileText.append(selectedFile.getName() + "\n");
            }
         }
      });

      frame.pack();
      frame.setVisible(true);
   }
}
