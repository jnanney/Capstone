import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.List;

public class Common
{
   public static final int CHAR_SIZE = 5;
   private Common() {}

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
