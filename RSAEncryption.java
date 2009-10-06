import java.io.File;
import java.util.ArrayList;
import java.math.BigInteger;
public class RSAEncryption 
{
   private String plaintext;
   private String cyphertext;

   public RSAEncryption(String message)
   {
      plaintext = message;   
      RSAKey key = new RSAKey(1024);
      chunkify(key.getPrimeProduct(), new BigInteger(plaintext)); 
   }


   public String[] splitInHalf(BigInteger number)
   {
      String temp = number.toString();
      int sublength = temp.length() / 2;
      temp  
   }
}
