import java.io.OutputStream;
import java.io.IOException;

public class SymmetricDataPacket implements PacketSpecificInterface
{
   private byte[] encryptedData;
   public SymmetricDataPacket(byte[] data)
   {
      encryptedData = OpenPGP.getMultiprecisionInteger(data, 0);
      System.out.println("In basic class it's " + java.util.Arrays.toString(encryptedData));
   }

   public SymmetricDataPacket(byte[] data, boolean isMPI)
   {
      System.out.println("In MPI class it's " + java.util.Arrays.toString(encryptedData));
      //this.encryptedData = data;
      this.encryptedData = (byte[]) data.clone();
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
