import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.DeflaterOutputStream;

public class FileEncryptor
{
   private File input;
   private ArrayList<OpenPGPPacket> encrypted;
   private RSABaseKey publicKey;
   private byte[] toEncrypt;

   public FileEncryptor(File input, RSABaseKey key) 
      throws FileNotFoundException, IOException, InvalidSelectionException
   {
      //literalData = new ArrayList<Byte>();
      this.input = input;
      this.publicKey=key;
      makeLiteralPacket(new FileInputStream(input));
      encryptFile();
   }
   
   private void compress(InputStream in) throws IOException
   {
      ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
      DeflaterOutputStream deflater = new DeflaterOutputStream(arrayOut);
      deflater.write(toEncrypt);
      toEncrypt = arrayOut.toByteArray();
   }

   public void write(File output) throws IOException, FileNotFoundException
   {
      FileOutputStream out = new FileOutputStream(output);
      for(OpenPGPPacket current : encrypted)
      {
         current.write(out);
      }
   }
   
   private void makeLiteralPacket(InputStream in) throws IOException
   {
      byte[] data = Common.readAllData(in);
      toEncrypt = data;
   }

   private void encryptFile() throws InvalidSelectionException
   {
      encrypted = new ArrayList<OpenPGPPacket>();
      byte[] fr = Common.makeLongBytes(0);
      long frLong = Common.makeBytesLong(fr);
      TripleDESEncryption des = new TripleDESEncryption(frLong);
      byte[] frEncrypted = Common.makeLongBytes(des.encrypt());
      byte[] randomData = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      Random rand = new Random();
      rand.nextBytes(randomData);
      System.out.println("Random data before enc " + java.util.Arrays.toString(randomData));
      byte[] cipher = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      for(int i = 0; i < OpenPGP.TRIPLEDES_BLOCK_BYTES; i++)
      {
         cipher[i] = (byte) (frEncrypted[i] ^ randomData[i]);
         fr[i] = cipher[i];
      }
      encrypted.addAll(createPackets(des, cipher));
      des = new TripleDESEncryption(Common.makeBytesLong(fr));
      frEncrypted = Common.makeLongBytes(des.encrypt());
      //We XOR the left two octets of frEncrypted with the last two octets of
      //random data.
      byte[] tempCipher = new byte[2];
      tempCipher[0] = (byte) (frEncrypted[0] ^ randomData[6]);
      tempCipher[1] = (byte) (frEncrypted[1] ^ randomData[7]);
      encrypted.addAll(createPackets(des, tempCipher));

      for(int i = 2; i < cipher.length; i++)
      {
         fr[i - 2] = cipher[i];
      }
      fr[6] = tempCipher[0];
      fr[7] = tempCipher[1];
      des = new TripleDESEncryption(Common.makeBytesLong(fr));
      frEncrypted = Common.makeLongBytes(des.encrypt());
      
      //We've encrypted 10 bytes of random data so now do the actual data
      cipher = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      for(int i = 0; i < toEncrypt.length; i += OpenPGP.TRIPLEDES_BLOCK_BYTES)
      {
         if(i + OpenPGP.TRIPLEDES_BLOCK_BYTES >= toEncrypt.length)
         {
            cipher = new byte[toEncrypt.length - i];
         }
         System.out.print("Plain is [" ); 
         for(int j = i, k = 0; j < i + cipher.length; j++, k++)
         {
            cipher[k] = (byte) (frEncrypted[k] ^ toEncrypt[j]);
            System.out.print(toEncrypt[j] + ", ");
            fr[k] = cipher[k];
         }
         encrypted.addAll(createPackets(des, cipher));
         System.out.println("\nfrE is " + java.util.Arrays.toString(frEncrypted));
         System.out.println("Encrypted is " + java.util.Arrays.toString(cipher));
         des = new TripleDESEncryption(Common.makeBytesLong(cipher));
         frEncrypted = Common.makeLongBytes(des.encrypt());
      }
      System.out.println("End of encryption\n\n\n");
   }

   private List<OpenPGPPacket> createPackets(TripleDESEncryption des, byte[] cipher)
   {
      ArrayList<OpenPGPPacket> result = new ArrayList<OpenPGPPacket>();

      EncryptedSessionKeyPacket key1 = new EncryptedSessionKeyPacket(publicKey,
                                           des.getKey1());
      EncryptedSessionKeyPacket key2 = new EncryptedSessionKeyPacket(publicKey,
                                           des.getKey2());
      EncryptedSessionKeyPacket key3 = new EncryptedSessionKeyPacket(publicKey,
                                           des.getKey3());
      SymmetricDataPacket symData = new SymmetricDataPacket(cipher, false);
      result.add(new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, key1));
      result.add(new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, key2));
      result.add(new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, key3));
      result.add(new OpenPGPPacket(OpenPGP.SYMMETRIC_DATA_TAG, symData));
      return result;
   }

}
