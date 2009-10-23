public class Junk
{
   public static void main(String[] args) throws Exception
   {
      char b = null;
      char c = null;
      char d = null;
      long temp = Common.makeLongFromChars('a', b, c, d);
      System.out.println(temp);
      String dude = Common.makeStringFromLong(temp);
      System.out.println(dude);
   }

}
