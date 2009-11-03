import java.util.List;
public class LiteralDataPacket implements PacketSpecificInterface
{
   private byte format;
   private String fileName;
   private List<Byte> literalData;
   private int date;

   public LiteralDataPacket(List<Byte> rawData)
   {
      format = rawData.get(0);
      int length = rawData.get(1);
      List<Byte> fileNameBytes = rawData.subList(2, 2 + length);
      fileName = "";
      for (Byte current : fileNameBytes)
      {
         fileName += String.valueOf((char) current.byteValue());
      }
      //TODO: get the data.
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
