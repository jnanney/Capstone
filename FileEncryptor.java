import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
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
   private long feedbackRegister;
   private byte[] toEncrypt;

   public FileEncryptor(File input, RSABaseKey key) 
      throws FileNotFoundException, IOException
   {
      //literalData = new ArrayList<Byte>();
      this.input = input;
      this.publicKey=key;
      feedbackRegister = 0;
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

   private void encryptFile(InputStream in) 
   {
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
