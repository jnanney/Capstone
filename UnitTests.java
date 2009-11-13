import org.junit.Assert;
import org.junit.Test;
public class UnitTests
{
   @Test public void basicDESTest() throws Exception
   {
      long original = 1234567;
      DESEncryption des = new DESEncryption(original);
      long encrypted = des.encrypt();
      DESEncryption desDecrypt = new DESEncryption(encrypted, des.getKey());
      long decrypted = desDecrypt.decrypt();
      Assert.assertTrue(encrypted != decrypted);
      Assert.assertTrue(decrypted == original);
   }

   @Test public void basic3DESTest() throws Exception
   {
      long original = 1234567;
      TripleDESEncryption des = new TripleDESEncryption(original);
      long encrypted = des.encrypt();
      des.changeOriginal(encrypted);
      long decrypted = des.decrypt();
      Assert.assertTrue(encrypted != decrypted);
      Assert.assertTrue(decrypted == original);
   }

   @Test public void byteValueTest()
   {
      int original = 255;
      byte byteCopy = (byte) original;
      Assert.assertTrue(Common.byteValue(byteCopy) == original);
   }

}
