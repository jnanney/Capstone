import java.io.OutputStream;
import java.io.IOException;
public class CompressedDataPacket implements PacketSpecificInterface
{
   public byte ZIP_CONSTANT = 1;
   private byte[] compressedData;

   public CompressedDataPacket(byte[] data) 
   {
      this.compressedData = new byte[data.length - 1];
      if(data[0] != ZIP_CONSTANT)
      {
         System.err.println("Compression algorithm constant " + data[0] + 
                            " invalid");
      }
      System.arraycopy(data, 1, compressedData, 0, compressedData.length);
   }

   public CompressedDataPacket(byte[] data, boolean notExisting)
   {
      this.compressedData = data;
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
