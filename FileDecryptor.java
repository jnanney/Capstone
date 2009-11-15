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
      throws MalformedPacketException, IOException, InvalidSelectionException
   {
      this.input = input;
      this.key = key;
      decryptFile();
   }
   
   public void decryptFile() throws MalformedPacketException, IOException, 
      InvalidSelectionException
   {
      ByteArrayOutputStream toBytes = new ByteArrayOutputStream();
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();
      long[] sessionKeys = getNextKeys(packets, 0);
      TripleDESEncryption des = new TripleDESEncryption(0, sessionKeys[0],
                                    sessionKeys[1], sessionKeys[2]);
      byte[] cipher = Common.makeLongBytes(des.encrypt());
      SymmetricDataPacket sym = (SymmetricDataPacket) packets.get(3).getPacket();
      byte[] encrypted = sym.getEncryptedData();
      byte[] random = new byte[8];
      for(int i = 0; i < encrypted.length; i++)
      {
         random[i] = (byte) (encrypted[i] ^ cipher[i]);
      }
      System.out.println("Encrypted length " + encrypted.length);
      System.out.println("Random data            " + java.util.Arrays.toString(random));

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
         EncryptedSessionKeyPacket sessionKey = (EncryptedSessionKeyPacket) 
                                                    packets.get(i).getPacket();
         
         rsa = new RSAEncryption(sessionKey.getEncryptedKey(), key);
         keys[j] = rsa.decrypt().longValue();
      }
      return keys;
   }
}
