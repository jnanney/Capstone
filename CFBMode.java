public class CFBMode
{
   /** The number of octets in a 3DES block */
   public static final int BLOCK_SIZE = 8;

   private TripleDESEncryption encryptor;
   private long text;
   public CFBMode(long text)
   {
   }

   public CFBMode(long text, TripleDESEncryption encryptor)
   {
      this.encryptor = encryptor;
      this.text = text;
   }

   public CFBMode(long text)
   {
      this.text = text;
   }

   public TripleDESEncryption getEncryptor()
   {
      return encryptor;
   }


}
