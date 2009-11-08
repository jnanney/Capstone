import java.math.BigInteger;
import java.io.*;
import java.util.List;
public class Test
{
   public static void main(String[] args) throws Exception
   {
      FileInputStream in = new FileInputStream("hello");
      byte[] array = new byte[3];
      in.read(array);
      RSAPrivateKey key = new RSAPrivateKey(1024);
      RSAEncryption rsa = new RSAEncryption(array, key); 
      BigInteger result = rsa.encrypt();
      System.out.println(result);
      FileOutputStream out = new FileOutputStream("andd");
      RSAEncryption munkey = new RSAEncryption(result.toByteArray(), key);
      BigInteger decrypt = munkey.decrypt();
      out.write(decrypt.toByteArray());
   }
}
