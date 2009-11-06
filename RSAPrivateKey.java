import java.util.Random;
import java.math.BigInteger;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;

public class RSAPrivateKey extends RSABaseKey implements PacketSpecificInterface
{
   private BigInteger prime1;
   private BigInteger prime2;
   private BigInteger totient;
   private BigInteger decryptionExponent;
   private byte[] checksum;

   public RSAPrivateKey(int bitLength)
   {
      //TODO: set the time
      byte[] keyTime = new byte[4];
      //compensate for not being unsigned
      int numBits = bitLength + 1;
      Random random = new Random();
      int primeCertainty = 100;
      prime1 = new BigInteger(numBits, primeCertainty, random);
      prime2 = new BigInteger(numBits, primeCertainty, random);
      BigInteger primeProduct = prime1.multiply(prime2);
      totient = (prime1.subtract(BigInteger.ONE)).multiply(prime2.subtract(BigInteger.ONE));
      BigInteger e;
      do
      {
         e = new BigInteger(32, primeCertainty, random);
         
      } while (!(e.gcd(totient)).equals(BigInteger.ONE));
      super.setEncryptionExponent(e);
      super.setPrimeProduct(primeProduct);
      super.setTime(keyTime);
      decryptionExponent = e.modInverse(totient);
      System.out.println("Generated d is "+ decryptionExponent);
      checksum = new byte[]{0, 0};
   }

   public RSAPrivateKey(byte[] data)
   {
      int i = super.readData(data);
      byte string2Key = data[i++]; //TODO: do something with this maybe
      byte[] mpi = OpenPGP.getMultiprecisionInteger(data, i);
      i += mpi.length + OpenPGP.MPI_LENGTH_BYTES;
      decryptionExponent = new BigInteger(mpi);
      
      mpi = OpenPGP.getMultiprecisionInteger(data, i);
      i += mpi.length + OpenPGP.MPI_LENGTH_BYTES;
      prime1 = new BigInteger(mpi);

      mpi = OpenPGP.getMultiprecisionInteger(data, i);
      i += mpi.length + OpenPGP.MPI_LENGTH_BYTES;
      prime2 = new BigInteger(mpi);
      checksum = new byte[2];
      checksum[0] = data[i++];
      checksum[1] = data[i++];
      System.out.println("Read in was " + decryptionExponent);
   }
   
   public BigInteger[] getPrimes()
   {
      BigInteger[] primes = new BigInteger[2];
      primes[0] = prime1;
      primes[1] = prime2;
      return primes;
   }

   public BigInteger getDecryptionExponent()
   {
      return decryptionExponent;
   }

   public String toString()
   {
      return "Primes are " + prime1 + " and " + prime2;
   }

   public boolean equals(Object object)
   {
      if(!(object instanceof RSAPrivateKey))
      {
         return false;
      }
      RSAPrivateKey key = (RSAPrivateKey) object;
      BigInteger[] primes = key.getPrimes();
      return (super.equals(key) && 
             decryptionExponent.equals(key.getDecryptionExponent()) &&
             (prime1.equals(primes[0]) || prime1.equals(primes[1])) &&
             (prime2.equals(primes[0]) || prime2.equals(primes[1])));
   }

   public void write(FileOutputStream output) throws IOException
   {
      super.write(output);
      byte[] dArray = OpenPGP.makeMultiprecisionInteger(decryptionExponent);
      byte[] pArray = OpenPGP.makeMultiprecisionInteger(prime1);
      byte[] qArray = OpenPGP.makeMultiprecisionInteger(prime2);
      checksum = new byte[]{0, 0}; //TODO: make this an actual checksum
      byte string2Key = 0;
      output.write(string2Key);
      output.write(dArray);
      output.write(pArray);
      output.write(qArray);
      //XXX supposed to write out a value u, but I don't know what it's used for
      output.write(checksum);
   }

   public int getBodyLength()
   {
      //1 Byte.SIZE for the string2key specifier.  Add 2 for each byte array
      return super.getBodyLength() + decryptionExponent.toByteArray().length +
             + prime1.toByteArray().length + prime2.toByteArray().length + 
             Byte.SIZE + checksum.length + 2 + 2 + 2;
   }

   public RSABaseKey getPublicKey()
   {
      return (RSABaseKey) super.clone();
   }
}
