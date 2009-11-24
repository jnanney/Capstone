public class SHA1
{
   private byte[] original;
   public static final int BLOCK_BITS = 512;

   /*public SHA1(byte[] data)
   {
      this.original = pad(data);
   }*/

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
      long MAX_UNSIGNED_INT = (Integer.MAX + 1) * 2;
      //Holds the maximum value of an int.
      long FULL_INT = 0xFFFFFFFFL;
      long total = (first & FULL_INT) + (second & FULL_INT);
      return total % MAX_UNSIGNED_INT;
   }
}
