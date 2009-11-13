import java.io.OutputStream;
import java.io.IOException;

public interface PacketSpecificInterface
{
   public void write(OutputStream output) throws IOException;
   public int getBodyLength();

}
