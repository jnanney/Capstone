public class SHA1
{
   private byte[] original;
   private static final int BLOCK_BITS = 512;
   private static final int FUNC_BOUND1 = 20;
   private static final int FUNC_BOUND2 = 40;
   private static final int FUNC_BOUND3 = 60;
   private static final int FUNC_BOUND4 = 80;

   public SHA1(byte[] data)
   {
      this.original = pad(data);
   }

   private byte[] pad(byte[] data)
   {
      int toAdd = (data.length ) % BLOCK_BITS;
      return null;
   }
   
   /**
    * This method does addition modulo 2^32 as required by the SHA-1 standard.
    * @param first - the first number to add
    * @param second - the second number to add
    * @return the two numbers added together mod 2^32
    * */
   private int addMod2(int first, int second)
   {
      //Integer.MAX holds 2^32 - 1 since it's giving the signed max.  
      //The unsigned value will be double of that + 1
      long MAX_UNSIGNED_INT = (Integer.MAX_VALUE + 1) * 2;
      //Holds the maximum value of an int.
      long FULL_INT = 0xFFFFFFFFL;
      long total = (first & FULL_INT) + (second & FULL_INT);
      return (int) (total % MAX_UNSIGNED_INT);
   }

   private int sha1Function(int iteration, int first, int second, int third)
   {
      if(iteration < 0 || iteration >= FUNC_BOUND4)
      {
         throw new InvalidSelectionException("Invalid iteration in SHA1");
      }
      else if (iteration < FUNC_BOUND1)
      {
         return (first & second) ^ (~first & third);
      }
      else if (iteration < FUNC_BOUND2)
      {
         return first ^ second ^ third;
      }
      else if (iteration < FUNC_BOUND3)
      {
         return (first & second) ^ (first & third) ^ (second & third);
      }
      else if (iteration < FUNC_BOUND4)
      {
         return (first ^ second ^ third);
      }
      else
      {
         return 0;
      }
   }

}
