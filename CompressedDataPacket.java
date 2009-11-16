import java.io.OutputStream;
import java.io.IOException;
public class CompressedDataPacket implements PacketSpecificInterface
{
   public byte ZIP_CONSTANT = 1;
   private byte[] compressedData;

   public CompressedDataPacket(byte[] data) throws MalformedPacketException
   {
      this.compressedData = new byte[data.length - 1];
      if(data[0] != ZIP_CONSTANT)
      {
         throw new MalformedPacketException("Compression algorithm with " +
            "constant " + data[0] + " is not supported");
      }
      System.arraycopy(data, 1, compressedData, 0, compressedData.length);
   }
   

   public byte[] getCompressedData()
   {
      return compressedData;
   }

   public int getBodyLength()
   {
      return compressedData.length + 1;
   }

   public void write(OutputStream out) throws IOException
   {
      out.write(ZIP_CONSTANT);
      out.write(compressedData);
   }
}
