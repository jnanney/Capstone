import java.util.Random;
import java.math.BigInteger;
import java.io.OutputStream;
import java.util.List;
import java.io.IOException;

public class RSABaseKey implements PacketSpecificInterface
{
   private BigInteger n;
   private BigInteger encryptionExponent;
   private byte[] time;
   private byte[] keyID;
   
   public RSABaseKey()
   {
      //TODO: create actual key ids
      keyID = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
      time = Common.getByteTime();
   }

   public RSABaseKey(BigInteger n, BigInteger encryptionExponent)
   {
      this.n = n;
      this.encryptionExponent = encryptionExponent;
      time = Common.getByteTime();
      keyID = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
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
      n = new BigInteger(1, mpi); //XXX

      mpi = OpenPGP.getMultiprecisionInteger(data, i);
      i += mpi.length + OpenPGP.MPI_LENGTH_BYTES;
      encryptionExponent = new BigInteger(1, mpi); //XXX
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

   public void setKeyID(byte[] keyID)
   {
      this.keyID = keyID;
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

   public void write(OutputStream output) throws IOException
   {
      byte version = 4;
      byte nArray[] = OpenPGP.makeMultiprecisionInteger(n);
      byte eArray[] = OpenPGP.makeMultiprecisionInteger(encryptionExponent);
      output.write(new byte[] {version});
      output.write(time);
      output.write(new byte[] {OpenPGP.RSA_CONSTANT});
      output.write(nArray);
      output.write(eArray);
   }

   public int getBodyLength()
   {
      //1 byte for a version number and 1 for constant identifying the 
      //algorithm as RSA. Add two for each of the arrays since they will be 
      //MPIs
      return time.length + n.toByteArray().length + 
             encryptionExponent.toByteArray().length + 1 + 1 + 2 + 2;
   }

   public byte[] getKeyID()
   {
      return keyID;
   }

   public Object clone()
   {
      return new RSABaseKey(n, encryptionExponent);
   }
}
