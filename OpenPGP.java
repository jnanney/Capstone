import java.math.BigInteger;
public class OpenPGP
{
   //The left two bits will always be 1 in a new format tag. So it should be 3
   //(which is 2 1-bits) shifted five places.
   public static final byte NEW_TAG_MASK = 3 << 5;
   public static final byte RSA_CONSTANT = 1;
   public static final byte TRIPLEDES_CONSTANT = 2;
   public static final byte PUBLIC_KEY_PACKET_TAG = 6 | NEW_TAG_MASK;
   public static final byte PRIVATE_KEY_PACKET_TAG = 5 | NEW_TAG_MASK;
   public static final byte LITERAL_DATA_PACKET_TAG = 11 | NEW_TAG_MASK;
   public static final byte PK_SESSION_KEY_TAG = 1 | NEW_TAG_MASK;
   public static final byte SYMMETRIC_DATA_TAG = 9 | NEW_TAG_MASK;
   public static final long MAX_ONE_OCTET = 191;
   public static final long MAX_TWO_OCTETS = 8383;
   public static final long MAX_FIVE_OCTETS = 0xFFFFFFFF;
   public static final byte PUBLIC_KEY_VERSION = 4;
   public static final byte TIME_BYTES = 4;

   public static void readPublicRSA(byte[] data, BigInteger encryptionExponent, 
      BigInteger n, byte[] time)
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
      byte[] mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      n = new BigInteger(mpi);

      mpiLength = (data[i++] << 8) | data[i++];
      mpiLength = mpiLength & 0xFFFF;
      mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      encryptionExponent = new BigInteger(mpi);
   }
}
