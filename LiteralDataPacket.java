import java.util.List;
import java.io.*;
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
      for (; i < 2 + length; i++)
      {
         fileName += (char) rawData[i];
      }
      date = new byte[DATE_SIZE];
      for(int j = 0; j < date.length; j++, i++)
      {
         date[j] = rawData[i];
      }
      literalData = new byte[rawData.length - i];
      for(int j = 0; i < rawData.length; i++, j++)
      {
         literalData[j] = rawData[i];
      }
      try{
      FileOutputStream xxx = new FileOutputStream("everydaynormal");
      xxx.write(literalData);
      }
      catch(Exception e)
      {
         System.out.println(e.getMessage());
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
}
