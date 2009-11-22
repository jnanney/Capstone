import java.io.OutputStream;
import java.io.IOException;
/**
 * Defines the interfaces that all packet specific classes must implement
 * @author Jonathan Nanney
 * */
public interface PacketSpecificInterface
{
   /** 
    * Write the body of the packet to the output stream.  This should not 
    * include the packet tag or length, that is written in the class that 
    * contains this one.
    * @param output - the output stream to write to
    * */
   public void write(OutputStream output) throws IOException;

   /**
    * Returns the length of the packet body in bytes.
    * @return the packet body length
    * */
   public int getBodyLength();

}
