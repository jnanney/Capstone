import java.io.FileOutputStream;
import java.io.IOException;

public interface PacketSpecificInterface
{
   public void write(FileOutputStream output) throws IOException;
   public int getBodyLength();

}
