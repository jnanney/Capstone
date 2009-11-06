import java.util.Random;
import java.math.BigInteger;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.IOException;

public class RSABaseKey implements PacketSpecificInterface
{
   private BigInteger n;
   private BigInteger encryptionExponent;
   private byte[] time;
   
   public RSABaseKey()
   {
   }

   public RSABaseKey(byte[] data)
   {
      readData(data);
   }

   public int readData(byte[] data)
   {
      time = new byte[4];
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
      byte[] mpi = OpenPGP.getMultiprecisionInteger(data, i);
      i += mpi.length + OpenPGP.MPI_LENGTH_BYTES;
      n = new BigInteger(mpi);

      mpi = OpenPGP.getMultiprecisionInteger(data, i);
      i += mpi.length + OpenPGP.MPI_LENGTH_BYTES;
      encryptionExponent = new BigInteger(mpi);
      return i;
   }

   public void setEncryptionExponent(BigInteger encryptionExponent)
   {
      this.encryptionExponent = encryptionExponent;
   }

   public void setTime(byte[] time)
   {
      this.time = time;
   }

   public void setPrimeProduct(BigInteger n)
   {
      this.n = n;
   }
   public BigInteger getEncryptionExponent()
   {
      return encryptionExponent;
   }

   public BigInteger getPrimeProduct()
   {
      return n;
   }

   public boolean equals(Object object)
   {
      if(!(object instanceof RSABaseKey))
      {
         return false;
      }
      RSABaseKey key = (RSABaseKey) object;
      return (encryptionExponent.equals(key.getEncryptionExponent()) &&
         n.equals(key.getPrimeProduct()));
   }

}
