import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.Random;

public class Test
{
   public static void main(String[] args) throws Exception
   {
      long num = 30821965994830L;
      num = num << 16;
      System.out.println(num);
      long temp = Common.getBits(num, 1,7);
      System.out.println(temp);
      System.out.println(Common.showBinary(temp));
   }
}
