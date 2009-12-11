import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.InflaterOutputStream;
import java.math.BigInteger;

/**
* This class is responsible for decrypting files
* @author Jonathan Nanney
* */
public class FileDecryptor
{
   /** The private key that is used to decrypt the file */
   private RSAPrivateKey key;
   /** The file to be decrypted */
   private File input;
   /** The data after it has been decrypted */
   private byte[] data;
   
   /**
* Constructor that takes a file and a key to decrypt with
* @param input - the file to decrypt
* @param key - the private key used to decrypt
* */
   public FileDecryptor(File input, RSAPrivateKey key)
      throws MalformedPacketException, IOException, InvalidSelectionException
   {
      this.input = input;
      this.key = key;
      //processRandomData();
      decryptFile();
      processResult();
   }
   
   
   /**
   * This method decrypts the 10 bytes of random data prefixed to the data
   * and returns the feedback register. Also does a quick check on data by
   * comparing the last 2 bytes of the random data and making sure they're
   * the same as the bytes before it.
   * @param packets - the list packets in the encrypted message
   * @return the feedback register which in this case will be bytes 3-10 of
   * the ciphertext
   * */
   /*private void processRandomData() throws MalformedPacketException, 
      FileNotFoundException, IOException
   {
      int packetCounter = 0; 
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();
      long sessionKeys[] = new long[OpenPGP.TRIPLEDES_KEYS];
      //TODO: get rid of magic numbers
      for(int i = 0; i < sessionKeys.length; i++)
      {
         EncryptedSessionKeyPacket sessKey = (EncryptedSessionKeyPacket) 
            packets.get(i).getPacket();
         RSAEncryption rsa = new RSAEncryption(
            sessKey.getEncryptedKey().toByteArray(), key);
         byte[] tempKey = rsa.decrypt().toByteArray();
         sessionKeys[i] = Common.makeBytesLong(tempKey);
         packetCounter++;
      }
      TripleDESEncryption des = new TripleDESEncryption(0, sessionKeys);
      byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
      byte[] fr = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      byte[] randomData = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      SymmetricDataPacket sym = (SymmetricDataPacket) 
         packets.get(packetCounter++).getPacket();
      byte[] cipher = sym.getEncryptedData();
      for(int i = 0; i < randomData.length; i++)
      {
         randomData[i] = (byte) (cipher[i] ^ frEncrypted[i]);
      }
      des.changeData(Common.makeBytesLong(cipher));
      //des = new TripleDESEncryption(Common.makeBytesLong(cipher),
      //                              getNextKeys(packets, 4));
      for(int i = 0; i < 6; i++)
      {
         fr[i] = cipher[i + 2];
      }
      SymmetricDataPacket secondSym = (SymmetricDataPacket)
         packets.get(packetCounter++).getPacket();
      cipher = secondSym.getEncryptedData();
      byte[] randomCheck = new byte[2];
      frEncrypted = Common.makeLongBytes(des.encrypt());
      for(int i = 0; i < cipher.length; i++)
      {
         randomCheck[i] = (byte) (cipher[i] ^ frEncrypted[i]);
      }
      fr[6] = cipher[0];
      fr[7] = cipher[1];
      if(randomCheck[0] != randomData[6] || randomCheck[1] != randomData[7])
      {
         throw new MalformedPacketException(
                  "Random data could not be duplicated");
      }
      //return fr;

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      //SymmetricDataPacket sym;
      for(int i = packetCounter; i < packets.size(); i ++)
      {
         des.changeData(Common.makeBytesLong(fr));
         //des = new TripleDESEncryption(Common.makeBytesLong(fr),
         //                              getNextKeys(packets, i));
         frEncrypted = Common.makeLongBytes(des.encrypt());
         //byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
         sym = (SymmetricDataPacket) packets.get(i).getPacket();
         //byte[] cipher = sym.getEncryptedData();
         cipher = sym.getEncryptedData();
         byte[] plain = new byte[cipher.length];
         for(int j = 0; j < plain.length; j++)
         {
            plain[j] = (byte) (frEncrypted[j] ^ cipher[j]);
         }
         out.write(plain);
         fr = cipher;
      }
      data = out.toByteArray();
      
   }*/
 
   
   public void decryptFile() throws MalformedPacketException, IOException
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();
      int i = 0;
      RSAEncryption rsa;
      byte[] fr = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      long[] sessionKeys = new long[OpenPGP.TRIPLEDES_KEYS];
      for(; i < OpenPGP.TRIPLEDES_KEYS && i < packets.size(); i++)
      {
         BigInteger sessKey = ((EncryptedSessionKeyPacket) 
            packets.get(i).getPacket()).getEncryptedKey();
         rsa = new RSAEncryption(sessKey.toByteArray(), key);
         sessionKeys[i] = Common.makeBytesLong(rsa.decrypt().toByteArray());
      }
      TripleDESEncryption des = new TripleDESEncryption(0, sessionKeys);
      byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
      SymmetricDataPacket sym = (SymmetricDataPacket) packets.get(i++).
         getPacket(); 
      byte[] cipher = sym.getEncryptedData();
      byte[] random = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      for(int j = 0; j < cipher.length; j++)
      {
         random[j] = (byte) (cipher[j] ^ frEncrypted[j]);
      }
      for(int j = 0; j < 6; j++)
      {
         fr[j] = cipher[j + 2];
      }
      des.changeData(Common.makeBytesLong(cipher));
      frEncrypted = Common.makeLongBytes(des.encrypt());
      sym = (SymmetricDataPacket) packets.get(i++).getPacket();
      cipher = sym.getEncryptedData();
      byte[] randomCheck = new byte[2];
      for(int j = 0; j < cipher.length; j++)
      {
         randomCheck[j] = (byte) (cipher[j] ^ frEncrypted[j]);
      }
      if(randomCheck[0] != random[6] || randomCheck[1] != random[7])
      {
         throw new MalformedPacketException(
                  "Random data could not be duplicated");
      }
      //Done with random data

      for(; i < packets.size(); i++)
      {
         des.changeData(Common.makeBytesLong(fr));
         frEncrypted = Common.makeLongBytes(des.encrypt());
         sym = (SymmetricDataPacket) packets.get(i).getPacket();
         cipher = sym.getEncryptedData();
         byte[] plain = new byte[cipher.length];
         for(int j = 0; j < cipher.length; j++)
         {
            plain[j] = (byte) (frEncrypted[j] ^ cipher[j]);
         }
         out.write(plain);
         fr = cipher;
      }
      data = out.toByteArray();
   }

   /**
   * Decrypts the file
   * */
   /*public void decryptFile() throws MalformedPacketException, IOException
   {
      //TODO: clean up code and remove magic numbers
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();
      byte[] fr = processRandomData(packets);
      TripleDESEncryption des;
      SymmetricDataPacket sym;
      for(int i = 8; i < packets.size(); i += 4)
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
         out.write(plain);
         fr = cipher;
      }
      data = out.toByteArray();
   }*/
   
   /**
   * This processes the file once it has been decrypted. After decryption
   * the file can still be compressed or it might be in a LiteralDataPacket
   * so this function acts appropriately depending on what has been decrypted
   * */
   private void processResult() throws MalformedPacketException, IOException
   {
      ByteArrayInputStream in = new ByteArrayInputStream(data);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PacketReader reader = new PacketReader(in);
      List<OpenPGPPacket> packets = reader.readPackets();
      while(packets.size() > 0)
      {
         OpenPGPPacket current = packets.get(0);
         if(current.getPacket() instanceof LiteralDataPacket)
         {
            LiteralDataPacket literal = (LiteralDataPacket) current.getPacket();
            out.write(literal.getLiteralData());
            packets.remove(0);
         }
         else if(current.getPacket() instanceof CompressedDataPacket)
         {
            CompressedDataPacket compressed = (CompressedDataPacket)
                                                current.getPacket();
            byte[] compressedData = compressed.getCompressedData();
            ByteArrayOutputStream decompressed = new ByteArrayOutputStream();
            InflaterOutputStream inflater = new InflaterOutputStream(
                                                 decompressed);
            inflater.write(compressedData, 0, compressedData.length);
            ByteArrayInputStream readData = new ByteArrayInputStream(
                                             decompressed.toByteArray());
            reader = new PacketReader(readData);
            List<OpenPGPPacket> morePackets = reader.readPackets();
            packets.addAll(morePackets);
            packets.remove(0);
         }
         else
         {
            System.out.println("Packet " + current + " is not supported");
         }
      }
      data = out.toByteArray();
   }
   
   /**
* Writes the decrypted file to a file
* @param output - the file to write to
* */
   public void write(File output) throws MalformedPacketException, IOException,
      InvalidSelectionException
   {
      FileOutputStream out = new FileOutputStream(output);
      out.write(data);
      out.close();
   }
}
