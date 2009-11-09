import java.math.BigInteger;
import java.io.*;
import java.util.List;
public class Test
{
   public static void main(String[] args) throws Exception
   {
      RSAPrivateKey key = new RSAPrivateKey(1024);
      FileEncryptor encryptor = new FileEncryptor(new File("hello"), key);
      encryptor.encryptFile();
      encryptor.write(new File("here"));

      FileDecryptor decryptor = new FileDecryptor(new File("here"), key);
      decryptor.write(new File("and"));
   }
}
