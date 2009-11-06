import java.io.FileOutputStream;
import java.util.List;
public class LiteralDataPacket implements PacketSpecificInterface
{
   private byte format;
   private String fileName;
   private byte[] literalData;
   private byte[] date;
   private int DATE_SIZE = 4;

   public LiteralDataPacket(byte[] rawData)
   {
      int i = 0;
      format = rawData[i++];
      int length = rawData[i++];
      fileName = "";
      for (; i < 2 + length && i < rawData.length; i++)
      {
         fileName += (char) rawData[i];
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

   public String getFileName()
   {
      return fileName;
   }

   public int getBodyLength()
   {
      //1 for format and one for fileName length
      return 1 + date.length + literalData.length + fileName.length() + 1;
   }

   public void write(FileOutputStream output)
   {
   }
}
