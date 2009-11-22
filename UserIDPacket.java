import java.io.OutputStream;
import java.io.IOException;
public class UserIDPacket implements PacketSpecificInterface
{
   private byte[] data;

   public int getBodyLength()
   {
      return 0;
   }

   public void write(OutputStream output) throws IOException
   {
   }
}
