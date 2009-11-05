public class Test
{
   public static void main(String[] args)
   {
      RSABaseKey key = new RSAPrivateKey(1024);
      if(key instanceof RSAKeyInterface)
      {
         System.out.println("Yes");
      }
   }
}
