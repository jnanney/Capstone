import java.io.OutputStream;
import java.io.IOException;

public class SymmetricDataPacket implements PacketSpecificInterface
{
   private byte[] encryptedData;
   public SymmetricDataPacket(byte[] data)
   {
      encryptedData = OpenPGP.getMultiprecisionInteger(data, 0);
   }

   public SymmetricDataPacket(byte[] data, boolean isMPI)
   {
      //this.encryptedData = data;
      this.encryptedData = data.clone();
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
      return encryptedData.length + 2;
   }
}
