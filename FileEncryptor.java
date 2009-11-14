import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
      throws FileNotFoundException, IOException
   {
      //literalData = new ArrayList<Byte>();
      this.input = input;
      this.publicKey=key;
      makeLiteralPacket(new FileInputStream(input));
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
//      encryptFile();
      FileOutputStream out = new FileOutputStream(output);
      for(OpenPGPPacket current : encrypted)
      {
         current.write(out);
      }
   }
   
   private void makeLiteralPacket(InputStream in) throws IOException
   {
      byte FORMAT = 0x62;
      byte[] data = Common.readAllData(in); 
      LiteralDataPacket literal = new LiteralDataPacket(FORMAT, 
                                  input.getName(), data);
      OpenPGPPacket literalDataPacket = new OpenPGPPacket(
                                    OpenPGP.LITERAL_DATA_PACKET_TAG, literal);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      literalDataPacket.write(out);
      toEncrypt = out.toByteArray();
   }

   private List<OpenPGPPacket> encryptFile(OutputStream out) 
      throws InvalidSelectionException
   {
      ArrayList<OpenPGPPacket> result = new ArrayList<OpenPGPPacket>();
      long feedbackRegister = 0;
      byte[] randomData = new byte[8];
      Random rand = new Random();
      rand.nextBytes(randomData);
      TripleDESEncryption des = new TripleDESEncryption(feedbackRegister);
      long frEncrypted = des.encrypt();
      long randomLong = Common.makeBytesLong(randomData);
      long cipherText = randomLong ^ frEncrypted;
      byte[] cipher = Common.makeLongBytes(cipherText);
      result.addAll(createPackets(des, cipher));
      feedbackRegister = cipherText;
      des = new TripleDESEncryption(feedbackRegister);
      frEncrypted = des.encrypt();

      //TODO: give better name
      byte[] temp = Common.makeLongBytes(frEncrypted);
      byte[] nextCipher = new byte[2];
      //randomData[6] and [7] have already been used but they are included 
      //again as a quick way to check the encryption.
      nextCipher[0] = temp[0] ^ randomData[6];
      nextCipher[1] = temp[1] ^ randomData[7];
      result.addAll(createPackets(des, nextCipher));


      feedbackRegister = makeBytesLong(cipherSoFar);
      des = new TripleDESEncryption(feedbackRegister);
      frEncrypted = des.encrypt();
      int i = 0;
      while(i < toEncrypt.length)
      {
      }


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

      SymmetricDataPacket symData = new SymmetricDataPacket(cipher);
      result.add(new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, key1));
      result.add(new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, key2));
      result.add(new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, key3));
      result.add(new OpenPGPPacket(OpenPGP.SYMMETRIC_DATA_TAG, symData));
      return result;
   }

   private List<OpenPGPPacket> encryptPacket(RSABaseKey rsaKey, byte[] data) 
      throws InvalidSelectionException
   {
      return null;
   }
}
