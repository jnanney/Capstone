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
      key = 0;
      Random random = new Random();
      for (int i = 0; i < KEY_LENGTH; i++)
      {
         key = key | random.nextInt(2);
         key = key << 1;
      }
      System.out.println(key);
   }

   public long getKey()
   {
      return key;
   }

}
