import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

public class FileDecryptor
{
   private RSAPrivateKey key;
   private File input;
   private ArrayList<Byte> data;

   public FileDecryptor(File input, RSAPrivateKey key) 
      throws MalformedPacketException, IOException
   {
      this.input = input;
      this.key = key;
      decryptFile();
   }
   
   public void decryptFile() throws MalformedPacketException, IOException
   {
      ByteArrayOutputStream toBytes = new ByteArrayOutputStream();
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();

   }

   public void write(File output) throws MalformedPacketException, IOException, 
      InvalidSelectionException
   {
   }

   private long[] getNextKeys(List<OpenPGPPacket> packets, int start)
   {
      long[] keys = new long[3];
      RSAEncryption rsa;
      for(int i = start, j = 0; i < packets.size() && j < keys.length; i++, j++)
      {
         (EncryptedSessionKeyPacket) sessionKey = (EncryptedSessionKeyPacket) 
                                                    packets.get(i).getPacket();
         
         rsa = new RSAEncryption(sessionKey.getEncryptedKey(), key);
         keys[j] = rsa.decrypt().longValue();
      }
      return keys;
   }
}
