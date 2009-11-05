import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.List;

public class Common
{
   public static final int CHAR_SIZE = 5;
   private Common() {}
   
   public static byte[] makeMultiprecisionInteger(BigInteger num)
   {
      byte[] temp = num.toByteArray();
      byte[] result = new byte[temp.length + 2];
      int numBits = temp.length * Byte.SIZE;
      result[0] = (byte) (numBits & 0xFF00);
      result[1] = (byte) (numBits & 0xFF);
      for(int i = 2; i < result.length; i++)
      {
         result[i] = temp[i - 2];
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

   public static long getNewFormatLength(int[] bytes)
   {
      long result = 0;
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
   
   public static long makeLongFromChars(char a, char b, char c, char d)
   {
      long result = 0;
      result = result | a;
      result = result << Character.SIZE;
      result = result | b;
      result = result << Character.SIZE;
      result = result | c;
      result = result << Character.SIZE;
      result = result | d;
      return result;
   }

   public static String makeStringFromLong(long input)
      throws InvalidSelectionException
   {
      String result = "";
      for(int i = 0; i < 4; i++)
      {
         char temp = (char) getBits(input, i*16 + 1, (i+1)*16);
         result += temp;
      }
      return result;
   }

   /**
    * Gets the bits from the starting to the ending position inclusive.  
    * Numbering starts at 1 and ends at 64.  Since Java is Big-Endian bit 1 is
    * the left most (and most significant) bit.
    * @param number - the number to get the bits from
    * @param start - the starting bit position 
    * @param end - the ending bit position 
    * @return a number containing the selected bits. 
    * */
   public static long getBits(long number, int start, int end) throws InvalidSelectionException
   {
      if (start > end || start < 1 || end > Long.SIZE + 1)
      {
         throw new InvalidSelectionException("Invalid bit positions");
      }
      long result = 0;
      for(int i = start; i <= end; i++)
      {
         result = result << 1;
         result = result | getBit(number, i);
      }
      return result;
   }



   public static byte getBit(byte number, int position)
      throws InvalidSelectionException
   {
      if (position > Byte.SIZE || position < 1)
      {
         throw new InvalidSelectionException(position + " is not a valid bit " + 
            "position");
      }
      byte mask = 1;
      int value = (mask << (Byte.SIZE - position)) & number;
      if (value > 0)
      {
         return 1;
      }
      else
      {
         return 0;
      }
   }

   /**
    * Gets either a 0 or a 1 from the given position.  Note that the leftmost
    * bit is bit 1 (not bit 0) because that's what the DES standard wants.
    * @param number - the number to get the bit from
    * @param position - the spot in the number where the bit is
    * @return a byte that stores 0 or 1 depending on the bit
    **/
   public static byte getBit(long number, int position) 
      throws InvalidSelectionException
   {
      if (position > Long.SIZE || position < 1)
      {
         throw new InvalidSelectionException(position + " is not a valid bit " + 
            "position");
      }
      long mask = 1;
      long value = (mask << (Long.SIZE - position)) & number;
      if (value != 0)
      {
         return 1;
      }
      else
      {
         return 0;
      }
   }

   public static long makeLong(int left, int right)
   {
      long result = ((long) left) << 32;
      result = result | right;
      return result;
   }

   public static long switchBits(long original, int newPositions[]) 
      throws InvalidSelectionException
   {
      long result = 0;
      for(int i = 0; i < newPositions.length; i++)
      {
         long bit = getBit(original, newPositions[i]);
         result = result | (bit << Long.SIZE - i - 1);
      }
      return result;
   }

   public static String showBinary(long num)
   {
      String result = "";
      while(num != 0)
      {
         result = (num & 1) + result;
         num = num >>> 1;
      }
      return result;
    
   }

   public static String makeNumberString(String string)
   {
      String result = "";
      char[] array = string.toCharArray();
      Formatter formatter = new Formatter();
      for(char current : array)
      {
         formatter.format("%05d", (int) current);
      }
      return formatter.toString();
   }

   public static String makeCharString(String string)
   {
      String result = "";
      for(int i = 0; i < string.length(); i += CHAR_SIZE)
      {
         String sub = string.substring(i, i + CHAR_SIZE);
         char c = (char) (Integer.valueOf(sub).intValue());
         result += c;
      }
      return result;
   }
   
   /*public static String[] split(String string, int subsize)
   {
	   int numElements = (int) Math.ceil((double) string.length() / subsize);
	   String result[] = new String[numElements];
	   for(int i = 0; i * subsize < string.length(); i++)
	   {
		   if(i * subsize + subsize > string.length())
		   {
			   result[i] = string.substring(i * subsize);
		   }
		   else
		   {
			   result[i] = string.substring(i * subsize, i * subsize + subsize);
		   }
	   }
	   return result;
   }

   /*public static String[] split(String string, int subsize)
   {
      ArrayList<String> result = new ArrayList<String>();
      for(int i = 0, current = 0; i < string.length(); i++)
      {
         if(
      }

   }*/

   public static String[] split(String string, int subsize)
   {
      int numElements = (int) Math.ceil((double) string.length() / subsize);
      String result[] = new String[numElements];
      for(int i = 0; i * subsize < string.length(); i++)
      {
         if(i * subsize + subsize > string.length())
         {
            result[i] = string.substring(i * subsize);
         }
         else
         {
            result[i] = string.substring(i * subsize, i * subsize + subsize);
         }
      }
      return result;
   }

   public static String addLeadingZeros(BigInteger num, int size)
   {
	   String result = num.toString();
	   while (result.length() % size != 0)
	   {
		   result = "0" + result;
	   }
	   return result;
   }
}
