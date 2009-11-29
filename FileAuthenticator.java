import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class FileAuthenticator
{
   private File input;
   private RSABaseKey key;

   public FileAuthenticator(File input, RSABaseKey key) //throws IOException
   {
      this.input = input;
      this.key = key;
   }

   public void write(File output) throws IOException, FileNotFoundException
   {
      if(! (key instanceof RSAPrivateKey))
      {
         System.err.println("You may not authenticate with a public key");
         System.exit(1);
      }
      byte FORMAT = 0x62;
      FileOutputStream out = new FileOutputStream(output);
      SignaturePacket sig = new SignaturePacket(input, (RSAPrivateKey) key);
      FileInputStream readIn = new FileInputStream(input);
      byte[] data = Common.readAllData(readIn);
      readIn.close();
      LiteralDataPacket literal = new LiteralDataPacket(FORMAT, 
         input.getName(), data);

      new OpenPGPPacket(OpenPGP.SIGNATURE_PACKET_TAG, sig).write(out);
      new OpenPGPPacket(OpenPGP.LITERAL_DATA_PACKET_TAG, literal).write(out);
      out.close();
   }

   public boolean check()
   {
      return true;
   }
}
