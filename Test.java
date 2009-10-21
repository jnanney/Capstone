import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.Random;

public class Test
{
   public static void main(String[] args) throws InvalidNumberException
   {
      DESEncryption des = new DESEncryption(12345); 
      System.out.println(des.encrypt());
      System.out.println(des.decrypt());
   }
}
