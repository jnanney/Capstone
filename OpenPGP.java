public class OpenPGP
{
   //The left two bits will always be 1 in a new format tag. So it should be 3
   //(which is 2 1-bits) shifted five places.
   public static final byte NEW_TAG_MASK = 3 << 5;
   public static final byte RSA_CONSTANT = 1;
   public static final byte TRIPLEDES_CONSTANT = 2;
   public static final byte PUBLIC_KEY_PACKET_TAG = 6 | NEW_TAG_MASK;
   public static final byte PRIVATE_KEY_PACKET_TAG = 5 | NEW_TAG_MASK;
   public static final byte LITERAL_DATA_PACKET_TAG = 11 | NEW_TAG_MASK;
   public static final byte PK_SESSION_KEY_TAG = 1 | NEW_TAG_MASK;
   public static final byte SYMMETRIC_DATA_TAG = 9 | NEW_TAG_MASK;
   public static final long MAX_ONE_OCTET = 191;
   public static final long MAX_TWO_OCTETS = 8383;
   public static final long MAX_FIVE_OCTETS = 0xFFFFFFFF;
   public static final byte PUBLIC_KEY_VERSION = 4;
   public static final byte TIME_BYTES = 4;
}
