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
      //System.out.println(des.decrypt());
    final int[] expandPositions =
   {
      32, 1, 2, 3, 4, 5,
      4, 5, 6, 7, 8, 9,
      8, 9, 10, 11, 12, 13,
      12, 13, 14, 15, 16, 17,
      16, 17, 18, 19, 20, 21,
      20, 21, 22, 23, 24, 25,
      24, 25, 26, 27, 28, 29,
      28, 29, 30, 31, 32, 1
   };
      
      /*long num = 2147483776L;
      num = num << 32;
      System.out.println(Common.showBinary(num));
      long temp = Common.switchBits(num, expandPositions);
      System.out.println(Common.showBinary(temp));*/
   }
}
