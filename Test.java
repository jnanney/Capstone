import java.math.BigInteger;
public class Test
{
   public static void main(String[] args)
   {
      byte[] num = new byte[]{0, 8, 64};
      BigInteger temp = null;
      byte[] newNum= OpenPGP.getMultiprecisionInteger(num, 0);
      temp = new BigInteger(newNum);
      System.out.println("number was " + temp);
   }

}
