import java.util.Random;
import java.math.BigInteger;

public class RSAKey
{
   private BigInteger prime1;
   private BigInteger prime2;
   private BigInteger n;
   private BigInteger encryptionExponent;

   public RSAKey(int numBits)
   {
      Random random = new Random();
      int primeCertainty = 100;
      prime1 = new BigInteger(numBits, primeCertainty, random);
      prime2 = new BigInteger(numBits, primeCertainty, random);
       
   }
   

   public String toString()
   {
      return "Prime 1: " + prime1 + "\nPrime 2: " + prime2;
   }
}
