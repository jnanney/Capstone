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
   private byte[] data; 

   public FileDecryptor(File input, RSAPrivateKey key) 
      throws MalformedPacketException, IOException, InvalidSelectionException
   {
      this.input = input;
      this.key = key;
      decryptFile();
   }
   
   private byte[] processRandomData(List<OpenPGPPacket> packets) 
      throws MalformedPacketException
   {
        
      TripleDESEncryption des = new TripleDESEncryption(0, 
                                     getNextKeys(packets, 0));
      byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
      byte[] fr = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      byte[] randomData = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      SymmetricDataPacket sym = (SymmetricDataPacket) packets.get(3).getPacket();
      byte[] cipher = sym.getEncryptedData();
      for(int i = 0; i < randomData.length; i++)
      {
         randomData[i] = (byte) (cipher[i] ^ frEncrypted[i]);
      }
      des = new TripleDESEncryption(Common.makeBytesLong(cipher), getNextKeys(packets, 4));
      for(int i = 0; i < 6; i++)
      {
         fr[i] = randomData[i + 2];
      }
      SymmetricDataPacket secondSym = (SymmetricDataPacket) packets.get(7).getPacket();
      cipher = secondSym.getEncryptedData();
      byte[] randomCheck = new byte[2];
      frEncrypted = Common.makeLongBytes(des.encrypt());
      for(int i = 0; i < cipher.length; i++)
      {
         randomCheck[i] = (byte) (cipher[i] ^ frEncrypted[i]);   
      }
      System.out.println(java.util.Arrays.toString(randomData));
      System.out.println(java.util.Arrays.toString(randomCheck));
      return null;
   }


   public void decryptFile() throws MalformedPacketException, IOException
   {
      FileOutputStream out = new FileOutputStream("kermit");
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();
      processRandomData(packets);
      byte[] fr = Common.makeLongBytes(0);
      TripleDESEncryption des;
      SymmetricDataPacket sym;
      for(int i = 0; i < packets.size(); i += 4)
      {
         des = new TripleDESEncryption(Common.makeBytesLong(fr), 
                                       getNextKeys(packets, i));
         byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
         sym = (SymmetricDataPacket) packets.get(i + 3).getPacket();
         byte[] cipher = sym.getEncryptedData();
         byte[] plain = new byte[cipher.length];
         for(int j = 0; j < plain.length; j++)
         {
            plain[j] = (byte) (frEncrypted[j] ^ cipher[j]);
         }
         System.out.println("Plain is " + java.util.Arrays.toString(plain));
         out.write(plain);
         if(i != 4)
         {
            fr = cipher;
         }
         else
         {
            byte[] second = cipher.clone();
            sym = (SymmetricDataPacket) packets.get(3).getPacket();
            byte[] first = sym.getEncryptedData();
            fr = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
            for(int j = 0; j < 6; j++)
            {
               fr[j] = first[j + 2];
            }
            fr[6] = second[0];
            fr[7] = second[1];
         }
      }
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
