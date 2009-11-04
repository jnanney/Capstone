import java.math.BigInteger;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import java.util.*;
public class Test
{
   public static void main(String[] args) throws Exception
   {
      FileOutputStream out = new FileOutputStream("catdog");
      out.write(new byte[] {OpenPGP.LITERAL_DATA_PACKET_TAG, 9});
      out.write(new byte[] {0x62, 0, 0,0,0,0, 65,66,67});
      PacketReader reader = new PacketReader(new File("catdog"));
      List<OpenPGPPacket> packets = reader.readPackets();
      for(OpenPGPPacket packet : packets)
      {
         System.out.println(packet);
      }
/*      RSAKey key = new RSAKey(1024);
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
      System.out.println("done");*/
      
   }
}
