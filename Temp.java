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
      byte num = -1;
      byte num2 = 4;
      byte result = (byte) (num ^ num2);
      System.out.println("Result was " + result);
   }
}
