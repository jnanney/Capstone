import java.util.Random;
import java.math.BigInteger;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.IOException;
public class RSAPublicKey implements PacketSpecificInterface, RSAKeyInterface
{
   private BigInteger n;
   private BigInteger encryptionExponent;
   private byte[] time;

   public RSAPublicKey(byte[] data)
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
      n = new BigInteger(mpi);

      mpiLength = (data[i++] << 8) | data[i++];
      mpi = new byte[mpiLength];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      encryptionExponent = new BigInteger(mpi);
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
