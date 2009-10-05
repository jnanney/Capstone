import java.util.Random;
import java.math.BigInteger;

public class RSAKey
{
   private BigInteger prime1;
   private BigInteger prime2;
   private BigInteger totient;
   private BigInteger n;
   private BigInteger encryptionExponent;
   private BigInteger decryptionExponent;

   public RSAKey(int numBits)
   {
      Random random = new Random();
      int primeCertainty = 100;
      prime1 = new BigInteger(numBits, primeCertainty, random);
      prime2 = new BigInteger(numBits, primeCertainty, random);
      BigInteger n = prime1.multiply(prime2);
      totient = (prime1.subtract(BigInteger.ONE)).multiply(prime2.subtract(BigInteger.ONE));
      do
      {
         encryptionExponent = new BigInteger(32, primeCertainty, random);
         
         System.out.println(encryptionExponent.gcd(totient));
      } while (!(encryptionExponent.gcd(totient)).equals(BigInteger.ONE));
      decryptionExponent = encryptionExponent.modInverse(totient);
   }

}
