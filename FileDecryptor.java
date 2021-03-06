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
   /** The file to write to, user may also call write with a filename */
   private File output;
   
   /**
* Constructor that takes a file and a key to decrypt with
* @param input - the file to decrypt
* @param key - the private key used to decrypt
* */
   public FileDecryptor(File input, RSAPrivateKey key)
   {
      this.input = input;
      this.key = key;
   }
   
   public FileDecryptor(File input, RSAPrivateKey key, File output)
   {
      this(input, key);
      this.output = output;
   }

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
      fr[6] = cipher[0];
      fr[7] = cipher[1];
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
   public void write(File outputFile) throws MalformedPacketException, 
      IOException, InvalidSelectionException
   {
      decryptFile();
      processResult();
      FileOutputStream out = new FileOutputStream(outputFile);
      out.write(data);
      out.close();
   }

   public void write() throws MalformedPacketException, IOException, 
      InvalidSelectionException
   {
      if(output != null)
      {
         this.write(output);
      }
   }

   public String getInputFilename()
   {
      return input.getName();
   }
}
