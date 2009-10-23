public class Junk
{
   public static void main(String[] args) throws Exception
   {
      TripleDESEncryption dude = new TripleDESEncryption(1234);
      long what=dude.encrypt();
      System.out.println(what);
      dude.changeOriginal(what);
      System.out.println(dude.decrypt());
   }

}
