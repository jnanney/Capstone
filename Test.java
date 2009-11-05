public class Test
{
   public static void main(String[] args)
   {
      byte temp = 3;
      temp += 192;
      byte and = (byte) 195;
      System.out.println(temp);
      System.out.println(and);
      /*RSABaseKey key = new RSAPrivateKey(1024);
      if(key instanceof RSABaseKey)
      {
         System.out.println("Yes");
      }*/
   }
}
