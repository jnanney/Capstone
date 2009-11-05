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
      int mpiLength = (data[i++] << 8) | data[i++];
      mpiLength = mpiLength & 0xFFFF;
      System.out.println("mpi length for n " + mpiLength);
      byte[] mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      n = new BigInteger(mpi);
      System.out.println("When reading it in it is " + n);
      mpiLength = (data[i++] << 8) | data[i++];
      mpiLength = mpiLength & 0xFFFF;
      System.out.println("mpi length for e " + mpiLength);
      mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      encryptionExponent = new BigInteger(mpi);
      //System.out.println("Encryption exponent " + encryptionExponent);
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

}
