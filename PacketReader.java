import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class PacketReader
{
   private File input;
   public PacketReader(File input)
   {
      this.input = input;
   }

   public List<OpenPGPPacket> readPackets() throws MalformedPacketException,
      IOException, FileNotFoundException
   {
      ArrayList<OpenPGPPacket> packets = new ArrayList<OpenPGPPacket>();
      FileInputStream readIn = new FileInputStream(input);
      byte tag = 0;
      int firstLengthOctet = 0;
      long length = 0;
      while(readIn.available() > 0)
      {
         tag = (byte) readIn.read(); 
         firstLengthOctet = readIn.read();
         if(firstLengthOctet <= OpenPGP.MAX_ONE_OCTET)
         {
            length = Common.getNewFormatLength(new int[] {firstLengthOctet});
         }
         else if(firstLengthOctet > OpenPGP.MAX_ONE_OCTET && firstLengthOctet <= 223)
         {
            int secondLengthOctet = readIn.read();
            length = Common.getNewFormatLength(new int[] {firstLengthOctet, 
               secondLengthOctet});
         }
         else if(firstLengthOctet == 255)
         {
            byte[] temp = new byte[4];
            int[] octets = new int[4];
            readIn.read(temp);
            //loop takes care of sign extension problems
            for(int i = 0; i < temp.length; i++)
            {
               octets[i] = temp[i] & 0xFF;
            }
            length = Common.getNewFormatLength(octets);
         }
         else
         {
            throw new MalformedPacketException("First octet is " + 
               firstLengthOctet);
         }

         long bytesRead = 0;
         byte[] data = new byte[(int) length];
         readIn.read(data);
         packets.add(new OpenPGPPacket(tag, data));
      }
      readIn.close();
      return packets;
   }
}