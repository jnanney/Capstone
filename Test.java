import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;

public class Test
{
   public static void main(String[] args) throws Exception
   {
      RSAKey key = new RSAKey(1024);
      key.writeToFile(new File("keyfile"));
      FileInputStream in = new FileInputStream(new File("keyfile"));
      FileWriter out = new FileWriter(new File("base64"));
      ArrayList<Byte> byteList = new ArrayList<Byte>();
      while(in.available() > 0)
      {
         byteList.add((byte) in.read());
      }
      byte array[] = new byte[byteList.size()];
      int i = 0;
      for(Byte temp : byteList)
      {
         array[i] = temp.byteValue();
         i++;
      }
      char[] test = Base64Coder.encode(array);
      System.out.println(test.length);
      out.write(test);
      out.flush();
      System.out.println("done");
      
   }
}
