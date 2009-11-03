public class OpenPGP
{
   //The left two bits will always be 1 in a new format tag. So it should be 3
   //(which is 2 1-bits) shifted five places.
   private static final byte NEW_TAG_MASK = 3 << 5;
   public static final byte RSA_CONSTANT = 1;
   public static final byte TRIPLEDES_CONSTANT = 2;
   public static final byte PUBLIC_KEY_PACKET_TAG = 6 | NEW_TAG_MASK;
   public static final byte PRIVATE_KEY_PACKET_TAG = 5 | NEW_TAG_MASK;
}
