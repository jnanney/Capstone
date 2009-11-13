public class DEFLATECompression
{
   public DEFLATECompression(byte[] data)
   {
   }
   

   public void weightBytes()
   {
      int[] weights = new int[255];
      java.util.Arrays.fill(weights, 0);
      for(int i = 0; i < data.length; i++)
      {
         weights[Common.byteValue(data[i])]++;
      }
   }

   public void generateTree()
   {
   }
}

