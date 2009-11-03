import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
public class FileEncryptor
{
   private File input;
   private File output;
   private ArrayList<Byte> literalData;
   private RSAKey publicKey;

   /*public FileEncryptor(File input, File output, RSAKey key)
   {
      this.input = input;
      this.output = output;
      this.publicKey=key;
   }*/
   public FileEncryptor(File input) throws Exception
   {
      this.input = input;
      makeLiteralPacket();
   }

   /*public void encryptFile()
   {
   }*/

   private void makeLiteralPacket() throws Exception
   {
      literalData = new ArrayList<Byte>();
      byte binary = 0x62;
      FileInputStream inputStream = new FileInputStream(input);
      long length = 1;
      while(inputStream.available() > 0)
      {
         literalData.add(Byte.valueOf((byte) inputStream.read()));
         length++;
      }
      byte[] bodyLength = Common.makeNewFormatLength(length);
      literalData.add(0, new Byte(binary));
      for(byte temp : bodyLength)
      {
         literalData.add(0, temp);
      }
      literalData.add(0, new Byte(OpenPGP.LITERAL_DATA_PACKET_TAG));
   }

}
