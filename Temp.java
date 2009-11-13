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
      byte[] num = Common.getByteTime();
      System.out.println("Time is " + Arrays.toString(num));
      FileEncryptor enc = new FileEncryptor(new File("hello"), new RSAPrivateKey(1024));
      PacketReader reader = new PacketReader(new File("guvna"));
      List<OpenPGPPacket> packets = reader.readPackets();
      for(OpenPGPPacket current : packets)
      {
         System.out.println(current);
      }
      LiteralDataPacket pack = (LiteralDataPacket) packets.get(0).getPacket();
      System.out.println("Format is " + pack.getFormat());
      System.out.println("Date is " + Arrays.toString(pack.getDate()));
      System.out.println("As long it is " + Common.makeBytesLong(pack.getDate()));
      System.out.println("Name is " + Arrays.toString(pack.getFileName()));
   }
}
