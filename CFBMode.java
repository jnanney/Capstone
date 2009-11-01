public class CFBMode
{
   /** The number of octets in a 3DES block */
   public static final int BLOCK_SIZE = 8;

   private TripleDESEncryption encryptor;

   public CFBMode(long text)
   {
   }

   public CFBMode(TripleDESEncryption encryptor)
   {
      this.encryptor = encryptor;
   }
}
