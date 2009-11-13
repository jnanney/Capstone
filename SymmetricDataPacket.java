import java.io.OutputStream;
import java.io.IOException;

public class SymmetricDataPacket implements PacketSpecificInterface
{
   private byte[] encryptedData;
   public SymmetricDataPacket(byte[] data)
   {
      encryptedData = data;
   }

   public SymmetricDataPacket(long data)
   {
      encryptedData = Common.makeLongBytes(data);
   }

   public byte[] getEncryptedData()
   {
      return encryptedData;
   }

   public void write(OutputStream output) throws IOException
   {
      output.write(OpenPGP.makeMultiprecisionInteger(encryptedData));
   }

   public int getBodyLength()
   {
      return encryptedData.length + OpenPGP.MPI_LENGTH_BYTES;
   }
}
