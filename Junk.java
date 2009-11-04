import java.util.*;
import java.io.*;
public class Junk
{
   public static void main(String[] args) throws Exception
   {
      FileEncryptor test = new FileEncryptor(new File("data"));
      PacketReader reader = new PacketReader(new File("out"));
      List<OpenPGPPacket> stuff = reader.readPackets();
      for(OpenPGPPacket temp : stuff)
      {
         System.out.println(temp);
      }
   }

}
