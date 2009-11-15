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
      byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
      SymmetricDataPacket sym = (SymmetricDataPacket) packets.get(3).getPacket();
      byte[] encrypted = sym.getEncryptedData();
      byte[] random = new byte[8];
      for(int i = 0; i < encrypted.length; i++)
      {
         random[i] = (byte) (encrypted[i] ^ frEncrypted[i]);
      }
      System.out.println("Random data            " + java.util.Arrays.toString(random));
      sessionKeys = getNextKeys(packets, 4);
      des = new TripleDESEncryption(Common.makeBytesLong(encrypted), sessionKeys[0], sessionKeys[1], sessionKeys[2]);
      frEncrypted = Common.makeLongBytes(des.encrypt()); 
      sym = (SymmetricDataPacket) packets.get(7).getPacket();
      encrypted = sym.getEncryptedData();
      System.out.println("Encrypted is " + java.util.Arrays.toString(encrypted));
      System.out.println("0 " + (byte) (frEncrypted[0] ^ encrypted[0]));
      System.out.println("1 " + (byte) (frEncrypted[1] ^ encrypted[1]));
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
