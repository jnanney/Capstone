import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class computes the SHA-1 hash as given in the FIPS-180 standard.  
 * @author Jonathan Nanney
 * */
public class SHA1
{
   /** The number of bytes in one block of data */
   private static final int BLOCK_BYTES = 64;
   /** For each block the hash function does 80 iterations */
   private static final int BLOCK_ITERATIONS = 80;
   /** The number of bytes used for the length of a padded message */
   private static final int LENGTH_BYTES = 8;
   /** Different results are given by the sha1Function depending on which 
    * iteration it is.  FUNC_BOUND1-4 give the boundaries at which the 
    * function switches to something else. So if the iteration is less than 20
    * the function will perform 1 computation but if it's exactly 20 it will 
    * perform a different one.*/
   private static final int FUNC_BOUND1 = 20;
   private static final int FUNC_BOUND2 = 40;
   private static final int FUNC_BOUND3 = 60;
   private static final int FUNC_BOUND4 = 80;
   
   /**
    * This function takes an array of data and returns a new array that has 
    * been padded to be a multiple of 512 bits.   The returned value will 
    * always have extra padding even if the original was a multiple of 512
    * bits.  After the original data there is a 1 bit, followed by some number
    * of 0 bits, followed by 8 bytes which gives the length of the original 
    * data in bits.
    * @param data - the original data
    * @return the original data plus padding to make it a multiple of 512 bits
    * */
   private static byte[] pad(byte[] data)
   {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      //the length of the data after the length bytes and the byte containing
      //a 1 bit have been padded on.
      int minLength = data.length + LENGTH_BYTES + 1;
      //The number of zeros to add to the data.
      int zerosToAdd = BLOCK_BYTES - (minLength % BLOCK_BYTES);
      try
      {
         byteStream.write(data);
         byteStream.write(1 << 7);
         byte[] zeros = new byte[zerosToAdd];
         Arrays.fill(zeros, (byte) 0);
         byteStream.write(zeros);
         byte[] temp = Common.makeLongBytes(data.length * Byte.SIZE);
         byteStream.write(Common.makeLongBytes(data.length * Byte.SIZE));
      }
      catch(IOException ioe)
      {
         System.err.println(ioe.getMessage());
      }
      return byteStream.toByteArray();
   }
   
   /**
    * Given some data this function computes the SHA-1 hash of the data.  
    * @param data - the data to be hashed
    * @return a hashed 160 bit number
    * */
   public static byte[] hash(byte[] data)
   {
      byte[] original = pad(data);
      int blocks = original.length / BLOCK_BYTES;
      int[] hashValues = new int[]{0x67452301, 0xefcdab89, 0x98badcfe, 
                                   0x10325476, 0xc3d2e1f0};
      for(int i = 0; i < blocks; i++)
      {
         int a = hashValues[0]; 
         int b = hashValues[1]; 
         int c = hashValues[2]; 
         int d = hashValues[3]; 
         int e = hashValues[4]; 
         int temp;
         int[] w = messageScheduler(original, i * BLOCK_BYTES);
         for(int j = 0; j < BLOCK_ITERATIONS; j++)
         {
            temp = addMod2(Common.rotateLeftCircular(a, 5), sha1Function(j, b, c, d)); 
            temp = addMod2(temp, e);
            temp = addMod2(temp, constant(j));
            temp = addMod2(temp, w[j]);
            e = d;
            d = c;
            c = Common.rotateLeftCircular(b, 30);
            b = a;
            a = temp;
         }
         hashValues[0] = addMod2(a, hashValues[0]);
         hashValues[1] = addMod2(b, hashValues[1]);
         hashValues[2] = addMod2(c, hashValues[2]);
         hashValues[3] = addMod2(d, hashValues[3]);
         hashValues[4] = addMod2(e, hashValues[4]);
      }
      byte[] result = new byte[20];
      int counter = 0;
      for(int i = 0; i < hashValues.length; i++)
      {
         byte[] temp = Common.makeIntBytes(hashValues[i]);
         for(int j = 0; j < temp.length; j++)
         {
            result[counter] = temp[j];
            counter++;
         }
      }
      return result;
   }

   /**
    * This method does addition modulo 2^32 as required by the SHA-1 standard.
    * @param first - the first number to add
    * @param second - the second number to add
    * @return the two numbers added together mod 2^32
    * */
   private static int addMod2(int first, int second)
   {
      //Integer.MAX holds 2^32 - 1 since it's giving the signed max.  
      //The unsigned value will be double of that + 1
      long MAX_UNSIGNED_INT = ((long) Integer.MAX_VALUE + 1) * 2;
      //Holds the maximum value of an int.
      long FULL_INT = 0xFFFFFFFFL;
      long total = (first & FULL_INT) + (second & FULL_INT);
      return (int) (total % MAX_UNSIGNED_INT);
   }
   
   /**
    * This function gives the result of the sha-1 function given in the 
    * standard.  The function used will be different depending on which 
    * iteration this is.  Order of first, second, and third doesn't matter.
    * @param iteration - which iteration from 0-79 we are on
    * @param first - one number to be used in the function
    * @param second - one number to be used in the function
    * @param third - one number to be used in the function
    * @return the result of the function.  
    * */
   private static int sha1Function(int iteration, int first, int second, 
      int third)
   {
      if(iteration < 0 || iteration >= FUNC_BOUND4)
      {
         throw new InvalidSelectionException("Invalid iteration in SHA1");
      }
      else if (iteration < FUNC_BOUND1)
      {
         return (first & second) ^ (~first & third);
      }
      else if (iteration < FUNC_BOUND2)
      {
         return first ^ second ^ third;
      }
      else if (iteration < FUNC_BOUND3)
      {
         return (first & second) ^ (first & third) ^ (second & third);
      }
      else if (iteration < FUNC_BOUND4)
      {
         return (first ^ second ^ third);
      }
      else
      {
         //Hopefully, this will never happen
         return 0;
      }
   }
   
   /**
    * Computes the message schedule as defined in the standard
    * @param data - an array with the data to use for the message schedule
    * @param start - the first array index to start getting data from
    * @return the message schedule as an array of size 80
    * */
   private static int[] messageScheduler(byte[] data, int start)
   {
      int BOUNDARY = 16;
      int PREV_INDEX1 = 3;
      int PREV_INDEX2 = 8;
      int PREV_INDEX3 = 14;
      int PREV_INDEX4 = 16;

      int[] result = new int[BLOCK_ITERATIONS];
      int bytesInInt = Integer.SIZE / Byte.SIZE;
      int i;
      for(i = 0; i < BOUNDARY; i++)
      {
         //((i+start) * 4), ((i+start) * 4) + bytesInInt);
         result[i] = Common.makeBytesInt(data, (i*4) + start, (i*4) + start + 4);
      }
      for(; i < result.length; i++)
      {
         int temp = result[i - PREV_INDEX1] ^ result[i - PREV_INDEX2] ^ 
             result[i - PREV_INDEX3] ^ result[i - PREV_INDEX4];
         result[i] = Common.rotateLeftCircular(temp, 1);
      }
      return result;
   }
   
   /**
    * Different arbitrary constants are used depending on the iteration.  
    * These constants are defined in the standard
    * @param iteration - which iteration from 0-79 we are in
    * @return the requested constant
    * */
   private static int constant(int iteration)
   {
      int FIRST_BOUNDARY = 20;
      int SECOND_BOUNDARY = 40;
      int THIRD_BOUNDARY = 60;
      if(iteration < FIRST_BOUNDARY)
      {
         return 0x5a827999;
      }
      else if(iteration < SECOND_BOUNDARY)
      {
         return 0x6ed9eba1;
      }
      else if(iteration < THIRD_BOUNDARY)
      {
         return 0x8f1bbcdc;
      }
      else
      {
         return 0xca62c1d6;
      }
   }
}
