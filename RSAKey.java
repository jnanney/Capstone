import java.util.Random;
import java.math.BigInteger;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
      numBits *= 2;
      Random random = new Random();
      int primeCertainty = 100;
      prime1 = new BigInteger(numBits, primeCertainty, random);
      prime2 = new BigInteger(numBits, primeCertainty, random);
      n = prime1.multiply(prime2);
      totient = (prime1.subtract(BigInteger.ONE)).multiply(prime2.subtract(BigInteger.ONE));
      do
      {
         encryptionExponent = new BigInteger(32, primeCertainty, random);
         
      } while (!(encryptionExponent.gcd(totient)).equals(BigInteger.ONE));
      decryptionExponent = encryptionExponent.modInverse(totient);
   }

   public BigInteger[] getPrimes()
   {
      BigInteger[] primes = new BigInteger[2];
      primes[0] = prime1;
      primes[1] = prime2;
      return primes;
   }

   public BigInteger getEncryptionExponent()
   {
      return encryptionExponent;
   }

   public BigInteger getDecryptionExponent()
   {
      return decryptionExponent;
   }

   public BigInteger getPrimeProduct()
   {
      return n;
   }

   public void writeToFile(File publicFile/*, File privateFile*/) throws Exception
   {
      byte publicTag = OpenPGP.PUBLIC_KEY_PACKET_TAG;
      byte version = 4;
      Calendar cal = new GregorianCalendar();
      long time = cal.get(Calendar.SECOND);
      //mask so it doesn't do sign extension when converting from int to long
      time = time & 0xFFFFFFFF;
      byte[] byteTime = new byte[4];
      for(int i = 0; i < byteTime.length; i++)
      {
         byteTime[i] = (byte) Common.getBits(time, (i * Byte.SIZE) + 1, ((i+1) * Byte.SIZE));
      }
      FileOutputStream publicOut = new FileOutputStream(publicFile);
      //FileOutputStream privateOut = new FileOutputStream(privateFile);
      //TODO: get the length
      publicOut.write(new byte[] {publicTag, length, version});
      publicOut.write(byteTime);
      publicOut.write(new byte[] {OpenPGP.RSA_CONSTANT});
   }

}
