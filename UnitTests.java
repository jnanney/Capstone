import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;

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

   @Test public void readsAll1() throws Exception
   {
      byte[] array = new byte[]{1,2,3,4,5,6,7,8,9};
      ByteArrayInputStream in = new ByteArrayInputStream(array);
      byte[] result = Common.readAllData(in);
      Assert.assertTrue(result != array);
      Assert.assertTrue(java.util.Arrays.equals(result, array));
   }

   @Test public void readsAll2() throws Exception
   {
      byte[] array = new byte[]{1,2,3,4,5,6,7,8,9};
      DeflaterInputStream deflate = new DeflaterInputStream(new ByteArrayInputStream(array));
      byte[] result = Common.readAllData(deflate);
      InflaterInputStream inflate = new InflaterInputStream(new ByteArrayInputStream(result));
      Assert.assertTrue(result != array);
      Assert.assertFalse(java.util.Arrays.equals(result, array));
      byte[] decompressed = Common.readAllData(inflate);
      Assert.assertTrue(decompressed != result);
      Assert.assertTrue(java.util.Arrays.equals(decompressed, array));
   }

}
