public class Junk
{
   public static void main(String[] args) throws Exception
   {
      /*DESKey key = new DESKey(-2445282691988019664L);
      key.keyScheduler(1);*/
      
      /*long dude = 15;
      System.out.println(Common.getBits(dude, 61, 65));*/
      DESEncryption des = new DESEncryption(1234);
      System.out.println(des.encrypt());
      System.out.println(des.decrypt());
   }
}
