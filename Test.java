import java.math.BigInteger;
import java.io.*;
import java.util.List;
public class Test
{
   public static void main(String[] args) throws Exception
   {
      /*
      RSAPrivateKey rsaKey = new RSAPrivateKey(1024);  
      EncryptedSessionKeyPacket skPack = new EncryptedSessionKeyPacket(rsaKey, "1234567");
      OpenPGPPacket packet = new OpenPGPPacket(OpenPGP.PK_SESSION_KEY_TAG, skPack);
      packet.write(new FileOutputStream("hello"));

      PacketReader reader = new PacketReader(new File("hello"));
      OpenPGPPacket and = reader.getPackets().get(0);
      */ 
      /*RSAPrivateKey key = new RSAPrivateKey(1024);
      RSABaseKey publicKey = key.getPublicKey();
      OpenPGPPacket publicPacket = new OpenPGPPacket(OpenPGP.PUBLIC_KEY_PACKET_TAG, publicKey);
      OpenPGPPacket privatePacket = new OpenPGPPacket(OpenPGP.PRIVATE_KEY_PACKET_TAG, key);
      publicPacket.write(new FileOutputStream("key.pub"));
      privatePacket.write(new FileOutputStream("key"));
      FileEncryptor encryptor = new FileEncryptor(new File("test.txt"), key);
      encryptor.encryptFile();
      encryptor.write(new File("output"));

      FileDecryptor dec = new FileDecryptor(new File("output"), null);
      dec.decrypt();*/

   }
}
