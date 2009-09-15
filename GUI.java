/**
 * Contains the main window for the application
 * Author - Jonathan Nanney
 **/

//Replace the * with the packages that I'm actually using once I know what 
//I'm actually using
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;

public class GUI
{
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
      JButton addFileButton = new JButton("Add File");  
      contentPane.add(addFileButton);

      addFileButton.addActionListener(new ActionListener() 
      {
         public void actionPerformed(ActionEvent evt)
         {
            chooser.showOpenDialog(frame);
         }
      });

      frame.pack();
      frame.setVisible(true);
   }
}
