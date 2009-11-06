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
   }

   public RSAPrivateKey(byte[] data)
   {
      int i = super.readData(data);
      byte string2Key = data[i++]; //TODO: do something with this maybe
      int mpiLength = (data[i++] << Byte.SIZE) | data[i++];
      mpiLength = 0xFFFF & mpiLength;
      byte[] mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      decryptionExponent = new BigInteger(mpi);

      mpiLength = (data[i++] << Byte.SIZE) | data[i++];
      mpiLength = 0xFFFF & mpiLength;
      mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      prime1 = new BigInteger(mpi);

      mpiLength = (data[i++] << Byte.SIZE) | data[i++];
      mpiLength = 0xFFFF & mpiLength;
      mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      prime2 = new BigInteger(mpi);
      i += 2; //TODO: this skips past the checksum. Do something with it
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

   public void writeToFile(File publicFile, File privateFile) throws 
      InvalidSelectionException, IOException, FileNotFoundException
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
      FileOutputStream privateOut = new FileOutputStream(privateFile);
      byte nArray[] = Common.makeMultiprecisionInteger(
         super.getPrimeProduct());
      byte eArray[] = Common.makeMultiprecisionInteger(
         super.getEncryptionExponent());
      long length = 1 + 4 + 1 + nArray.length + eArray.length;
      byte[] lengthBytes = Common.makeNewFormatLength(length);
      publicOut.write(new byte[] {publicTag});
      publicOut.write(lengthBytes);
      //Start writing the key specific stuff 
      publicOut.write(new byte[] {version});
      publicOut.write(byteTime);
      publicOut.write(new byte[] {OpenPGP.RSA_CONSTANT});
      publicOut.write(nArray);
      publicOut.write(eArray);
      publicOut.close();

      byte[] dArray = Common.makeMultiprecisionInteger(decryptionExponent);
      byte[] pArray = Common.makeMultiprecisionInteger(prime1);
      byte[] qArray = Common.makeMultiprecisionInteger(prime2);
      byte[] checksum = new byte[]{0, 0}; //TODO: make this an actual checksum
      byte string2Key = 0;
      length += 1 + checksum.length + dArray.length + pArray.length +
         qArray.length;
         //+ uArray.length; XXX: can't figure out why this is necessary
      lengthBytes = Common.makeNewFormatLength(length);
      privateOut.write(new byte[] {OpenPGP.PRIVATE_KEY_PACKET_TAG});
      privateOut.write(lengthBytes);
      privateOut.write(new byte[] {version});
      privateOut.write(byteTime);
      privateOut.write(new byte[] {OpenPGP.RSA_CONSTANT});
      privateOut.write(nArray);
      privateOut.write(eArray);
      privateOut.write(string2Key);
      privateOut.write(dArray);
      privateOut.write(pArray);
      privateOut.write(qArray);
      privateOut.write(checksum);
      privateOut.close();
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

}
