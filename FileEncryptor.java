import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileEncryptor
{
   private File input;
   private ArrayList<Byte> literalData;
   private ArrayList<OpenPGPPacket> encrypted;
   //private byte[] literalData;
   private RSABaseKey publicKey;

   public FileEncryptor(File input, RSABaseKey key) throws FileNotFoundException, IOException
   {
      this.input = input;
      this.publicKey=key;
      makeLiteralPacket();
   }

   public void encryptFile() throws InvalidSelectionException
   {
      encrypted = new ArrayList<OpenPGPPacket>();
      for(int i = 0; i < literalData.size(); i += OpenPGP.TRIPLEDES_BLOCK_BYTES) 
      {

         int subListEnd = i + 8 > literalData.size() ? literalData.size() : 8 + i;
         List<Byte> block = literalData.subList(i, subListEnd);
         byte[] primitiveBlock = Common.makeByteListPrimitive(block); 
         OpenPGPPacket[] packets = encryptPacket(publicKey, primitiveBlock);
         encrypted.add(packets[0]);
         encrypted.add(packets[1]);
      }
   }

   public void write(File output) throws IOException, FileNotFoundException
   {
      FileOutputStream out = new FileOutputStream(output);
      for(OpenPGPPacket current : encrypted)
      {
         current.write(out);
      }
   }


   private void makeLiteralPacket() throws FileNotFoundException, IOException
   {
      /*FileInputStream readIn = new FileInputStream(input);
      byte[] fileName = input.getName().getBytes("UTF-8");
      literalData = new byte[readIn.available() + 1+1 + fileName.length];
      int i = 0;
      literalData[i++] = 0x62;
      literalData[i++] = fileName.length;
      for(int j = 0; j < fileName.length; i++, j++)
      {
         literalData[i] = fileName[j];
      }
      readIn.read(literalData, i, literalData.length);*/
      literalData = new ArrayList<Byte>();
      byte binary = 0x62;
      FileInputStream inputStream = new FileInputStream(input);
      long length = 1;
      while(inputStream.available() > 0)
      {
         literalData.add(Byte.valueOf((byte) inputStream.read()));
         length++;
      }
      byte[] bodyLength = OpenPGP.makeNewFormatLength(length);
      literalData.add(new Byte((byte) 0));
      literalData.add(new Byte((byte) 0));
      literalData.add(new Byte((byte) 0));
      literalData.add(new Byte((byte) 0));
      literalData.add(new Byte((byte) 0));
      literalData.add(0, new Byte(binary));
      for(byte temp : bodyLength)
      {
         literalData.add(0, temp);
      }
      literalData.add(0, new Byte(OpenPGP.LITERAL_DATA_PACKET_TAG));
   }

   public OpenPGPPacket[] encryptPacket(RSABaseKey rsaKey, byte[] data) 
      throws InvalidSelectionException
   {
      long message = Common.makeBytesLong(data);
      TripleDESEncryption des = new TripleDESEncryption(message);
      DESKey[] desKeys = des.getKeys();
      String keys = "" + desKeys[0] + desKeys[1] + desKeys[2];
      long encrypted = des.encrypt();
      byte[] encryptedBytes = Common.makeLongBytes(encrypted);
      EncryptedSessionKeyPacket encryptedKeys = new EncryptedSessionKeyPacket(rsaKey, keys);
      SymmetricDataPacket symData = new SymmetricDataPacket(encryptedBytes);
      OpenPGPPacket sessionKey = new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, encryptedKeys);
      OpenPGPPacket encryptedData = new OpenPGPPacket(OpenPGP.SYMMETRIC_DATA_TAG, symData);
      return new OpenPGPPacket[] { sessionKey, encryptedData};
   }
}
