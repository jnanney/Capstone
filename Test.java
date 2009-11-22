import java.math.BigInteger;
import java.io.*;
import java.util.List;
public class Test
{
   public static void main(String[] args) throws Exception
   {
      if(args.length != 4)
      {
         System.err.println("Usage: java Test <original> <encrypted file> <decrypted file> <iteration>");
         System.exit(1);
      }
      RSAPrivateKey key = new RSAPrivateKey(1024);
      FileEncryptor encryptor = new FileEncryptor(new File(args[0]), key);
      encryptor.write(new File(args[1]));

      FileDecryptor decryptor = new FileDecryptor(new File(args[1]), key);
      decryptor.write(new File(args[2]));

      Runtime rt = Runtime.getRuntime();
      Process p = rt.exec("diff " + args[0] + " " + args[2]);
      InputStream in = p.getInputStream();
      while (in.available() > 0)
      {
         System.out.println(in.read());
      }
      if(p.waitFor() != 0)
      {
         System.err.println("Bad diff");
         OpenPGPPacket keyToWrite = new OpenPGPPacket(OpenPGP.PRIVATE_KEY_PACKET_TAG, key);
         keyToWrite.write(new FileOutputStream("tests/last" + args[3]));
         /*PacketReader reader = new PacketReader(new File("last"));
         reader.readPackets();*/
         //System.exit(p.exitValue());
      }
      //System.exit(0);
   }
}
