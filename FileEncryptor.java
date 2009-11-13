import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileEncryptor
{
   private File input;
   private ArrayList<OpenPGPPacket> encrypted;
   private RSABaseKey publicKey;
   private long feedbackRegister;

   public FileEncryptor(File input, RSABaseKey key) 
      throws FileNotFoundException, IOException
   {
      //literalData = new ArrayList<Byte>();
      this.input = input;
      this.publicKey=key;
      feedbackRegister = 0;
   }


   public void write(File output) throws IOException, FileNotFoundException
   {
//      encryptFile();
      FileOutputStream out = new FileOutputStream(output);
      for(OpenPGPPacket current : encrypted)
      {
         current.write(out);
      }
   }
   
   private void makeLiteralPacket(InputStream in)
   {

   }

   private void encryptFile(InputStream in) 
   {
   }

   private List<OpenPGPPacket> encryptPacket(RSABaseKey rsaKey, byte[] data) 
      throws InvalidSelectionException
   {
      ArrayList<OpenPGPPacket> result = new ArrayList<OpenPGPPacket>();
      TripleDESEncryption des = new TripleDESEncryption(feedbackRegister);
      //the feedback register encrypted
      long frEncrypted = des.encrypt();
      for(int i = 0; i < data.length; i++)
      {
      }
      return null;      
   }

}
