import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.math.BigInteger;

public class FileDecryptor
{
   private RSAPrivateKey key;
   private File input;
   private ArrayList<Byte> data;

   public FileDecryptor(File input, RSAPrivateKey key)
   {
      this.input = input;
      this.key = key;
   }

   public void decrypt() throws MalformedPacketException, IOException
   {
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> encrypted = reader.readPackets();
      for(int i = 0; i < encrypted.size(); i+=2)
      {
         EncryptedSessionKeyPacket sKey = (EncryptedSessionKeyPacket) encrypted.get(i).getPacket();
         SymmetricDataPacket data = (SymmetricDataPacket) encrypted.get(i+1).getPacket();
         byte[] byteData = data.getEncryptedPacket();

      }
   }

   public void write(File output)
   {
   }
}
