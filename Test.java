import java.math.BigInteger;
import java.io.*;
import java.util.List;
public class Test
{
   public static void main(String[] args) throws Exception
   {
      RSAPrivateKey key = new RSAPrivateKey(1024);
      FileEncryptor encryptor = new FileEncryptor(new File("hello"), key);
      //encryptor.encryptFile();
      encryptor.write(new File(args[0]));

      FileDecryptor decryptor = new FileDecryptor(new File(args[0]), key);
      //decryptor.write(new File(args[1]));

      /*Runtime rt = Runtime.getRuntime();

      Process p = rt.exec("diff hello.tmp " + args[1]);
      p.waitFor();
      if(p.exitValue() != 0)
      {
         key.write(new FileOutputStream("last"));
         System.exit(p.exitValue());
      }
      System.exit(0);*/
   }
}
