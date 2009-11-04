import java.util.Random;
import java.math.BigInteger;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.IOException;
public class RSAPrivateKey implements PacketSpecificInterface, RSAKeyInterface
{
   private BigInteger prime1;
   private BigInteger prime2;
   private BigInteger totient;
   private BigInteger n;
   private BigInteger encryptionExponent;
   private BigInteger decryptionExponent;
   private byte[] time;

   public RSAPrivateKey(int bitLength)
   {
      //compensate for not being unsigned
      int numBits = bitLength + 1;
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

   public RSAPrivateKey(byte[] data)
   {
      int i = 0; 
      if(data[i++] != OpenPGP.PUBLIC_KEY_VERSION)
      {
         System.err.println("Given public key version is not supported");
      }
      for(int j = 0; j < OpenPGP.TIME_BYTES; j++, i++)
      {
         time[j] = data[i];
      }
      if(data[i++] != OpenPGP.RSA_CONSTANT)
      {
         System.err.println("Only RSA is currently supported");
      }
      int mpiLength = (data[i++] << 8) | data[i++];
      byte[] mpi = new byte[mpiLength];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
   //   n = 
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
      byte nArray[] = n.toByteArray();
      byte eArray[] = encryptionExponent.toByteArray();
      long length = 1 + 4 + nArray.length + eArray.length;
      byte[] lengthBytes = Common.makeNewFormatLength(length);
      publicOut.write(new byte[] {publicTag});
      publicOut.write(lengthBytes);
      publicOut.write(new byte[] {version});
      publicOut.write(byteTime);
      publicOut.write(new byte[] {OpenPGP.RSA_CONSTANT});
      publicOut.write(nArray);
      publicOut.write(eArray);
   }
}