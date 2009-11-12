import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileEncryptor
{
   private File input;
   //private ArrayList<Byte> literalData;
   private ArrayList<OpenPGPPacket> encrypted;
   private byte[] literalData;
   private RSABaseKey publicKey;
   private long feedbackRegister;

   public FileEncryptor(File input, RSABaseKey key) 
      throws FileNotFoundException, IOException
   {
      //literalData = new ArrayList<Byte>();
      this.input = input;
      this.publicKey=key;
      makeLiteralPacket();
      feedbackRegister = 0;
   }


   public void write(File output) throws IOException, FileNotFoundException
   {
      encryptFile();
      FileOutputStream out = new FileOutputStream(output);
      for(OpenPGPPacket current : encrypted)
      {
         current.write(out);
      }
   }


   private void makeLiteralPacket() throws FileNotFoundException, IOException
   {
      FileInputStream readIn = new FileInputStream(input);
      literalData = new byte[readIn.available() + 
         OpenPGP.TRIPLEDES_BLOCK_BYTES + 2];
      byte[] randomData = new byte[OpenPGP.TRIPLEDES_BLOCK_BYTES];
      Random random = new Random();
      random.nextBytes(randomData);
      for(int i = 0; i < randomData.length; i++)
      {
         literalData[i] = randomData[i];
      }
      //bytes 9 and 10 should be the same as bytes 7 and 8 so we can do a quick
      //check 
      literalData[OpenPGP.TRIPLEDES_BLOCK_BYTES + 1] = 
            literalData[OpenPGP.TRIPLEDES_BLOCK_BYTES - 1];
      literalData[OpenPGP.TRIPLEDES_BLOCK_BYTES + 2] = 
            literalData[OpenPGP.TRIPLEDES_BLOCK_BYTES];


   }

   /*private void makeLiteralPacket() throws FileNotFoundException, IOException
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
/*      byte binary = 0x62;
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
      literalData.add(0, new Byte(OpenPGP.LITERAL_DATA_PACKET_TAG));*/
    /*  FileInputStream inputStream = new FileInputStream(input);
      while(inputStream.available() > 0)
      {
         literalData.add(Byte.valueOf((byte) inputStream.read()));
      }
   }*/

   private void encryptFile() 
   {
      encrypted = new ArrayList<OpenPGPPacket>();
      int i = 0;
      while(i < literalData.length)
      {

      }
   }

   private List<OpenPGPPacket> encryptPacket(RSABaseKey rsaKey, byte[] data) 
      throws InvalidSelectionException
   {
      ArrayList<OpenPGPPacket> result = new ArrayList<OpenPGPPacket>();
      TripleDESEncryption des = new TripleDESEncryption(feedbackRegister);
      //the feedback register encrypted
      long frEncrypted = des.encrypt();
      for(int i = 0; i < data.length; i++)
      {
      }
      return null;      
   }

}
