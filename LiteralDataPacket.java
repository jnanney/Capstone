import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
public class LiteralDataPacket implements PacketSpecificInterface
{
   private byte format;
   private byte[] fileName;
   private byte[] literalData;
   private byte[] date;
   private int DATE_SIZE = 4;

   //TODO: check that filenames are not greater than 255 bytes.   
   public LiteralDataPacket(byte format, String fileName, byte[] literalData)
   {
      this.fileName = fileName.getBytes();
      this.format = format;
      this.literalData = literalData;
      this.date = Common.getByteTime();
   }

   public LiteralDataPacket(byte[] rawData)
   {
      int i = 0;
      format = rawData[i++];
      int length = rawData[i++];
      fileName = new byte[length];
      for (int j = 0; j < length && i < rawData.length; i++, j++)
      {
         fileName[j] = rawData[i];
      }
      date = new byte[DATE_SIZE];
      for(int j = 0; j < date.length && i < rawData.length; j++, i++)
      {
         date[j] = rawData[i];
      }
      literalData = new byte[rawData.length - i];
      for(int j = 0; i < rawData.length; i++, j++)
      {
         literalData[j] = rawData[i];
      }
   }

   public byte[] getDate()
   {
      return date;
   }

   public byte[] getLiteralData()
   {
      return literalData;
   }

   public byte getFormat()
   {
      return format;
   }

   public int getBodyLength()
   {
      //1 for format and one for fileName length
      return 1 + date.length + literalData.length + fileName.length + 1;
   }

   public void write(OutputStream output) throws IOException
   {
      output.write(new byte[]{format, (byte) fileName.length});
      output.write(fileName);
      output.write(date);
      output.write(literalData);
   }
}
