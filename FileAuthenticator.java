import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.math.BigInteger;

public class FileAuthenticator
{
   private File input;
   private RSABaseKey key;
   /** The number of packets that must be in a signed message */
   private static final int SIGNED_PACKETS = 2;

   public FileAuthenticator(File input, RSABaseKey key) //throws IOException
   {
      this.input = input;
      this.key = key;
   }

   public void signAndWrite(File output) throws IOException, FileNotFoundException
   {
      if(! (key instanceof RSAPrivateKey))
      {
         System.err.println("You may not authenticate with a public key");
         System.exit(1);
      }
      byte FORMAT = 0x62;
      FileOutputStream out = new FileOutputStream(output);
      SignaturePacket sig = new SignaturePacket(input, (RSAPrivateKey) key);
      FileInputStream readIn = new FileInputStream(input);
      byte[] data = Common.readAllData(readIn);
      readIn.close();
      LiteralDataPacket literal = new LiteralDataPacket(FORMAT, 
         input.getName(), data);

      new OpenPGPPacket(OpenPGP.SIGNATURE_PACKET_TAG, sig).write(out);
      new OpenPGPPacket(OpenPGP.LITERAL_DATA_PACKET_TAG, literal).write(out);
      out.close();
   }

   public boolean check() throws MalformedPacketException, IOException
   {
      PacketReader reader = new PacketReader(input);
      List<OpenPGPPacket> packets = reader.readPackets();
      if(packets.size() != SIGNED_PACKETS)
      {
         throw new MalformedPacketException("Not a signed file");
      }
      SignaturePacket sig = (SignaturePacket) packets.get(0).getPacket();
      LiteralDataPacket lit = (LiteralDataPacket) packets.get(1).getPacket();
      BigInteger signatureData = sig.getSignature();
      byte[] literalData = lit.getLiteralData();
      byte[] hashedLiteralData = SHA1.hash(literalData);
      RSAEncryption rsa = new RSAEncryption(signatureData.toByteArray(), key);
      BigInteger hashedSignData = rsa.encrypt();
      BigInteger realHashed = new BigInteger(Common.SIGN, hashedLiteralData);
      return realHashed.equals(hashedSignData);
   }
}
