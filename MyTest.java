import java.io.*;
import java.util.*;
public class MyTest
{
   public static void main(String[] args) throws Exception
   {
      PacketReader reader = new PacketReader(new File("last"));
      List<OpenPGPPacket> packets = reader.readPackets();
      RSAPrivateKey key = (RSAPrivateKey) packets.get(0).getPacket();
      FileEncryptor enc = new FileEncryptor(new File("hello"), key);
      enc.write(new File("wow"));
      FileDecryptor dec = new FileDecryptor(new File("wow"), key);
      //FileDecryptor dec = new FileDecryptor(new File("tests/enc5"), key);
      dec.write(new File("guvna"));
   }
}
