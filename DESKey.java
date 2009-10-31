import java.util.Random;

public class DESKey
{

   private int[] permutedChoice1C =
   {
      57, 49, 41, 33, 25, 17, 9,
      1, 58, 50, 42, 34, 26, 18,
      10, 2, 59, 51, 43, 35, 27,
      19, 11, 3, 60, 52, 44, 36
   };

   private int[] permutedChoice1D = 
   {
      63, 55, 47, 39, 31, 23, 15,
      7, 62, 54, 46, 38, 30, 22,
      14, 6, 61, 53, 45, 37, 29,
      21, 13, 5, 28, 20, 12, 4
   };

   private int[] permutedChoice1 = 
   {
      57, 49, 41, 33, 25, 17, 9,
      1, 58, 50, 42, 34, 26, 18,
      10, 2, 59, 51, 43, 35, 27,
      19, 11, 3, 60, 52, 44, 36,
      63, 55, 47, 39, 31, 23, 15,
      7, 62, 54, 46, 38, 30, 22,
      14, 6, 61, 53, 45, 37, 29,
      21, 13, 5, 28, 20, 12, 4
   };

   private int[] permutedChoice2 = 
   {
      14, 17, 11, 24, 1, 5,
      3, 28, 15, 6, 21, 10,
      23, 19, 12, 4, 26, 8,
      16, 7, 27, 20, 13, 2,
      41, 52, 31, 37, 47, 55,
      30, 40, 51, 45, 33, 48,
      44, 49, 39, 56, 34, 53,
      46, 42, 50, 36, 29, 32
   };

   private int[] numShifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
   private long key;
   private static final int KEY_LENGTH = 64;
   
   public DESKey()
   {
     generateKey();
   }

   public DESKey(long key)
   {
      this.key = key;
   }

   private void generateKey()
   {
      //TODO: make every 8th bit a 1 or 0 depending on parity
      key = 0;
      Random random = new Random();
      int oneBits = 0;
      for (int i = 0; i < KEY_LENGTH; i++)
      {
         long temp = (long) random.nextInt(2) << i;
         if (temp > 0)
         {
            oneBits++; 
         }
         if((i+1) % 8 == 0)
         {
            temp = (long) ((oneBits + 1) % 2) << i;
            oneBits = 0;
         }
         key = key | temp;
      }
   }

   public long getKey()
   {
      return key;
   }

   public long keyScheduler(int iteration) 
      throws InvalidSelectionException
   {
      int MIN_ITERATION = 1;
      int MAX_ITERATION = numShifts.length;

      if (iteration < MIN_ITERATION || iteration > MAX_ITERATION)
      {
         throw new InvalidSelectionException(iteration + " is not valid");
      }
      long c = Common.switchBits(key, permutedChoice1C);
      long d = Common.switchBits(key, permutedChoice1D);
      c = c >>> 36; //Shift 36 so that c and d will be on the right side
      d = d >>> 36;
      for(int i = 0; i < iteration; i++)
      {
         for(int j = 0; j < numShifts[i]; j++)
         {
            c = shift28Rotational(c);
            d = shift28Rotational(d);
         }
      }
      long mask = 0xFFFFFFF;
      long result = ((c << 28) | d) << 8;
      result = (Common.switchBits(result, permutedChoice2)) >>> 16;
      return result;
   }

   public long shift28Rotational(long num)
   {
      long mask = 0xFFFFFFF;
      return (mask & (num << 1 | num >>> 27));
   }

}

