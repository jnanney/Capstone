import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.List;

public class Common
{
   public static final int CHAR_SIZE = 5;
   private Common() {}
   
   public static int[] splitLong(long num)
   {
      int result[] = new int[2];
      long mask = 0xFFFFFFFF; //4 solid bytes
      result[0] = (int) (num & mask);
      result[1] = (int) ((num >> 32) & mask);
      return result;
   }

   public static int rotationalLeftShift(int num, int shiftLength)
   {
      //Note: I did not think this up.  This was taken from a question at
      //Stackoverflow available at: http://tinyurl.com/yktm9dq
      return (num << shiftLength | num >> Integer.SIZE);
   }

   public static long switchBits(long original, int newPositions[])
   {
      long result = 0;
      long temp = 0;
      for(int i = 0; i < newPositions.length; i++)
      {
         long mask = 1;
         int shiftValue = newPositions.length - newPositions[i];
         mask = mask << shiftValue;
         if ((original & mask) > 0)
         {
            temp = 1;
         }
         else
         {
            temp = 0;
         }
         int newShift = newPositions.length - i - 1;
         result = result | (temp << newShift);
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
   
   /*public static List<String> split(String string, int subsize)
   {
      int element_size = 5;
      ArrayList<String> result = new ArrayList<String>();
      String current = "";
      for (int i = 0; i < string.length(); i += element_size)
      {
         if (current.length() + element_size > subsize)
         {
            result.add(current);
            current = "";
         }
         if (i + element_size > string.length())
         {
            current += string.substring(i);
         }
         else
         {
            current += string.substring(i, i + element_size);
         }
      }
      return result;
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
