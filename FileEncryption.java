/*import java.io.FileInputStream;
import java.io.FileOutputStream;
public class FileEncryption
{
   private FileInputStream inputStream;
   private FileOutputStream outputStream;

   public FileEncryption(String inputFile, String outputFile)
   {
      inputStream = new FileInputStream(inputFile);
      outputStream = new FileOutputStream(outputFile);
   }

   public void encryptFile()
   {
      byte[] current = new byte[Long.SIZE / Byte.SIZE];
      while(inputStream.available() > 0)
      {
         inputStream.read(current);
      }
   }
}*/
