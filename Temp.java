import java.util.Random;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;
public class Temp
{
   public static void main(String[] args) throws Exception
   {
      byte[] array = new byte[]{1,2,3,4,5,6,7,8,9};
      DeflaterInputStream deflate = new DeflaterInputStream(new ByteArrayInputStream(array));
      byte[] result = Common.readAllData(deflate);
      System.out.println(java.util.Arrays.toString(result));
      InflaterInputStream inflate = new InflaterInputStream(new ByteArrayInputStream(result));
      byte[] decompressed = Common.readAllData(inflate);
      System.out.println(java.util.Arrays.toString(decompressed));
   }
}
