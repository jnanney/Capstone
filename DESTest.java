import org.junit.Assert;

public class DESTest
{
   @org.junit.Test public void basicTest() throws Exception
   {
      long original = 1234567;
      DESEncryption des = new DESEncryption(original);
      long encrypted = des.encrypt();
      DESEncryption desDecrypt = new DESEncryption(encrypted, des.getKey());
      long decrypted = desDecrypt.decrypt();
      org.junit.Assert.assertTrue(encrypted != decrypted);
      org.junit.Assert.assertTrue(decrypted == original);
   }
}
