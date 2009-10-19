import java.util.Random;

public class DESKey
{
    private long key;
    private static final int KEY_LENGTH = 64;
    
   public DESKey()
   {
     generateKey();
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
         key = key | temp;
      }
   }

   public long getKey()
   {
      return key;
   }

}
