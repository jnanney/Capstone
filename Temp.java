import java.util.Random;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;
import java.io.*;
import java.util.*;
public class Temp
{
   public static void main(String[] args) throws Exception
   {
      long num = 1285;
      byte[] result = Common.makeLongBytes(num);
      System.out.println(Arrays.toString(result));
   }
}
