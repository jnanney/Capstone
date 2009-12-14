import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class will read packets from an input stream
 * @author Jonathan Nanney
 * */
public class PacketReader
{
   /** The input stream to read from */
   private InputStream readIn;
   
   /**
    * Constructor that takes an input stream to read from
    * @param input - the input stream to read from
    * */
   public PacketReader(InputStream input)
   {
      this.readIn = input;
   }
   
   /**
    * Constructor that takes a file and creates a new FileInputStream
    * @param input - the file to create an input stream from
    * */
   public PacketReader(File input) throws FileNotFoundException
   {
      this.readIn = new FileInputStream(input);
   }
   
   /**
    * Reads packets from the input stream and returns them in a list
    * @return the list of packets that were read
    * */
   public List<OpenPGPPacket> readPackets() throws MalformedPacketException,
      IOException
   {
      ArrayList<OpenPGPPacket> packets = new ArrayList<OpenPGPPacket>();
      byte tag = 0;
      int firstLengthOctet = 0;
      long length = 0;
      while(readIn.available() > 0)
      {
         tag = (byte) readIn.read(); 
         firstLengthOctet = readIn.read();
         if(firstLengthOctet <= OpenPGP.MAX_ONE_OCTET)
         {
            length = OpenPGP.getNewFormatLength(new int[] {firstLengthOctet});
         }
         else if(firstLengthOctet > OpenPGP.MAX_ONE_OCTET && 
                 firstLengthOctet <= 223)
         {
            int secondLengthOctet = readIn.read();
            length = OpenPGP.getNewFormatLength(new int[] {firstLengthOctet, 
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
               octets[i] = temp[i] & Common.BYTE_MASK;
            }
            length = OpenPGP.getNewFormatLength(octets);
         }
         else
         {
            throw new MalformedPacketException("First octet is " + 
               firstLengthOctet);
         }
         byte[] data = new byte[(int) length];
         int amount = readIn.read(data);
         OpenPGPPacket temp = new OpenPGPPacket(tag, data);
         packets.add(new OpenPGPPacket(tag, data));
      }
      readIn.close();
      return packets;
   }
}
