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
   public static final byte COMPRESSED_DATA_TAG = 8 | NEW_TAG_MASK;
   public static final long MAX_ONE_OCTET = 191;
   public static final long MAX_TWO_OCTETS = 8383;
   public static final long MAX_FIVE_OCTETS = 0xFFFFFFFF;
   public static final byte PUBLIC_KEY_VERSION = 4;
   public static final byte TIME_BYTES = 4;
   public static final byte MPI_LENGTH_BYTES = 2; 
   public static final byte TRIPLEDES_BLOCK_BYTES = 8;
   
   public static byte[] getMultiprecisionInteger(byte[] data, int start)
   {
      int i = start;
      int mpiLength = (data[i++] << Byte.SIZE) | data[i++];
      mpiLength = 0xFFFF & mpiLength;
      byte[] mpi = new byte[mpiLength / Byte.SIZE];
      for(int j = 0; j < mpi.length && i < data.length; i++, j++)
      {
         mpi[j] = data[i];
      }
      return mpi;
   }

   public static byte[] makeMultiprecisionInteger(BigInteger num)
   {
      byte[] temp = num.toByteArray();
      byte[] result = new byte[temp.length + 2];
      int numBits = temp.length * Byte.SIZE;
      result[0] = (byte) (numBits >> 8);
      result[1] = (byte) (numBits & 0xFF);
      for(int i = 2; i < result.length; i++)
      {
         result[i] = temp[i - 2];
      }
      return result;
   }

   public static byte[] makeMultiprecisionInteger(byte[] num)
   {
      byte[] result = new byte[num.length + OpenPGP.MPI_LENGTH_BYTES];
      int numBits = num.length * Byte.SIZE;
      result[0] = (byte) (numBits >> 8);
      result[1] = (byte) (numBits & 0xFF);
      for(int i = OpenPGP.MPI_LENGTH_BYTES; i < result.length; i++)
      {
         result[i] = num[i - OpenPGP.MPI_LENGTH_BYTES];
      }
      return result;
   }
    
   public static byte[] makeNewFormatLength(long length)

   {
      byte[] result = new byte[0];
      if(length <= OpenPGP.MAX_ONE_OCTET)
      {
         result = new byte[1];
         result[0] = (byte) length;
      }
      else if(length <= OpenPGP.MAX_TWO_OCTETS)
      {
         result = new byte[2];
         long first = (length >>> 8) + 191;
         long second = (length & 0xFF) - 192;
         result[0] = (byte) (first & 0xFF);
         result[1] = (byte) (second & 0xFF);
      }
      else if(length <= OpenPGP.MAX_FIVE_OCTETS)
      {
         result = new byte[5];
         result[0] = (byte) 0xFF;
         int mask = 0xFF;
         for(int i = 1; i <result.length; i++)
         {
            result[i] = (byte) (length & (mask << ((4-i) * 8)));
         }
      }
      return result;
   }

   public static int getNewFormatLength(int[] bytes)
   {
      int result = 0;
      if(bytes.length == 1)
      {
         result = bytes[0];
         result = result & 0xFF;
      }
      else if(bytes.length == 2)
      {
         result = ((bytes[0] - 192) << 8) + bytes[1] + 192;
         result = result & 0xFFFF;
      }
      else if(bytes.length == 5)
      {
         result = (bytes[1] << 24) | (bytes[2] << 16) | (bytes[3] << 8) | bytes[4];
         result = 0xFFFFFFFF;
      }
      return result;
   }
}
