import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
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

   public void write(File output) throws MalformedPacketException, IOException, 
      InvalidSelectionException
   {
      FileOutputStream out = new FileOutputStream(output);
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> encrypted = reader.readPackets();
      for(int i = 0; i < encrypted.size(); i += 4)
      {
         EncryptedSessionKeyPacket keyPack1 = (EncryptedSessionKeyPacket) encrypted.get(i).getPacket();
         EncryptedSessionKeyPacket keyPack2 = (EncryptedSessionKeyPacket) encrypted.get(i+1).getPacket();
         EncryptedSessionKeyPacket keyPack3 = (EncryptedSessionKeyPacket) encrypted.get(i+2).getPacket();
         BigInteger encKey1 = keyPack1.getEncryptedKey();
         BigInteger encKey2 = keyPack2.getEncryptedKey();
         BigInteger encKey3 = keyPack3.getEncryptedKey();
         

         RSAEncryption rsa = new RSAEncryption(encKey1, key);
         BigInteger key1 = rsa.decrypt();
         rsa.switchOriginal(encKey2);
         BigInteger key2 = rsa.decrypt();
         rsa.switchOriginal(encKey3);
         BigInteger key3 = rsa.decrypt();
         
         long key1Long = key1.longValue();
         long key2Long = key2.longValue();
         long key3Long = key3.longValue();
         System.out.println("Key 1 decrypted " + key1Long);
         System.out.println("Key 2 decrypted " + key2Long);
         System.out.println("Key 3 decrypted " + key3Long);
         SymmetricDataPacket symData = (SymmetricDataPacket) encrypted.get(i+3).getPacket();
         byte[] encryptedData = symData.getEncryptedData();
         long toDecrypt = Common.makeBytesLong(encryptedData);
         TripleDESEncryption des = new TripleDESEncryption(toDecrypt, key1Long, key2Long, key3Long);
         long result = des.decrypt();
         byte[] toWrite = Common.makeLongBytes(result);
         out.write(toWrite);
      }
   }

}
