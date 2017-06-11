package rbac.utils;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class RSAUtil {

    /*
     * 注：
     * 1、明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这是不分片情况下的.分片后,密文长度=密钥长度*片数； 该类暂不支持分片；
     * 2、android系统的RSA实现是"RSA/None/NoPadding"，而标准JDK实现是"RSA/None/PKCS1Padding" ，
     * 这造成了在android机上加密后无法在服务器上解密的原因（服务器使用的是SUN JDK6.0）。其实只要加载bouncycastle Jar到PC服务端和android客户端即可。
     * 例：KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
     * 3、JDK支持的密钥长度：RSA keys must be at least 512 bits long； RSA keys must be no longer than 16384 bits
     * 4、使用X509EncodedKeySpec生成公钥、使用PKCS8EncodedKeySpec私钥， X509和PKCS8的区别：
     *
     * 两者同属第一代PKI（Public Key Infrastructure 公钥基础设施）标准，同属第一代还有... 第二代是在2001年，
     * 由微软、VeriSign和webMethods三家公司发布了XML密钥管理规范（XML Key Management Specification，XKMS），被称为第二代PKI标准。
     *
     * X.509是由国际电信联盟（ITU-T）制定的数字证书标准。在X.500确保用户名称惟一性的基础上，X.509为X.500用户名称提供了通信实体的鉴别机制，
     * 并规定了实体鉴别过程中广泛适用的证书语法和数据接口。
     * X.509的最初版本公布于1988年。X.509证书由用户公共密钥和用户标识符组成。此外还包括版本号、证书序列号、CA标识符、签名算法标识、签发者名称、证书有效期等信息。
     * 这一标准的最新版本是X.509 v3，它定义了包含扩展信息的数字证书。该版数字证书提供了一个扩展信息字段，用来提供更多的灵活性及特殊应用环境下所需的信息传送。
     *
     * PKCS是由美国RSA数据安全公司及其合作伙伴制定的一组公钥密码学标准，其中包括证书申请、证书更新、证书作废表发布、扩展证书内容以及数字签名、数字信封的格式等方面的一系列相关协议。
     * PKCS#8：描述私有密钥信息格式，该信息包括公开密钥算法的私有密钥以及可选的属性集等。
     * 其他常见的标准有：
     * PKCS#7：定义一种通用的消息语法，包括数字签名和加密等用于增强的加密机制，PKCS#7与PEM兼容，所以不需其他密码操作，就可以将加密的消息转换成PEM消息。
     * PKCS#10：描述证书请求语法。
     * PKCS#12：描述个人信息交换语法标准。描述了将用户公钥、私钥、证书和其他相关信息打包的语法。
     *
     * 此外，
     * CA中心普遍采用的规范是X.509[13]系列和PKCS系列
     * 本质上，X509、PKCS12都是数字文档，需要通过配合一定的二进制编码使用，如DER、PEM（用于Base64编码，如各种X509 v3证书）
     * 文件保存时，DER、PEM也可以作为文件拓展名，也可使用其他的拓展名，如CRT（DER或PEM编码）、CER（CRT的微软版）、KEY（DER或PEM编码的PKCS#8公钥或私钥）
     *
     * 5、私钥保密，公钥可以公开；利用私钥可以生成公钥
     * 6、由于RSA加密速度慢的问题，在实际应用中，往往配合对成加密算法使用（用RSA加密其密钥）
     * 7、用RSA对信息摘要进行加密来生成“数字签名”；由CA机构统一发布的包含公钥等信息的文件称为“数字证书”
     */

    public static final String ALGORITHM = "RSA";
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    public static KeyPair generateKey(int keysize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGen.initialize(keysize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    public static KeyPair generateKey() throws Exception {
        return generateKey(2048);
    }

    public static PublicKey generatePublicKey(BigInteger modulus, BigInteger exponent) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        return  keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey generatePrivateKey(BigInteger modulus, BigInteger exponent) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
        return  keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey generatePublicKey(String modulus, String exponent) throws Exception {
        return generatePublicKey(new BigInteger(modulus), new BigInteger(exponent));
    }

    public static PrivateKey generatePrivateKey(String modulus, String exponent) throws Exception {
        return generatePrivateKey(new BigInteger(modulus), new BigInteger(exponent));
    }

    public static PublicKey generatePublicKey(byte[] key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        return  keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey generatePrivateKey(byte[] key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        return  keyFactory.generatePrivate(keySpec);
    }

    public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    public static byte[] encrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    public static byte[] decrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    public static void main(String[] args) throws Exception {

        KeyPair keyPair = generateKey();
        PublicKey publicKey =  keyPair.getPublic();
        PrivateKey privateKey =  keyPair.getPrivate();

        System.out.println(publicKey);
        System.out.println(privateKey);

        System.out.println("public key:" + Arrays.toString(publicKey.getEncoded()));
        System.out.println("private key:" + Arrays.toString(privateKey.getEncoded()));

        System.out.println(Arrays.toString(encrypt("明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这".getBytes(), publicKey)));
        System.out.println(new String(decrypt(encrypt("明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这".getBytes(), publicKey), privateKey)));

        System.out.println(Arrays.toString(encrypt("明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这".getBytes(), privateKey)));
        System.out.println(new String(decrypt(encrypt("明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这".getBytes(), privateKey), publicKey)));


        /*
         * 控制台输出：
         *
         * Sun RSA public key, 1024 bits
         *   modulus: 93699138040937916463395372497585331399055623974580119199881127129466164364447768740295613953054654558895894904106586273898073474786196351074354681012249468975636558090521850437011111367162722488851724589794325761338960084185753894204462287740361118090965827221300205295990050180311354621154174402012794058829
         *   public exponent: 65537
         * sun.security.rsa.RSAPrivateCrtKeyImpl@fff053a6
         * public key:[48, -127, -97, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -127, -115, 0, 48, -127, -119, 2, -127, -127, 0, -123, 110, -105, -87, 112, 110, -77, -79, 95, -92, 61, -101, 49, -34, 123, 20, 32, -6, -31, -90, 126, -15, 7, -89, 62, 112, -14, 57, -66, -35, -45, -111, -90, 33, 72, -119, -102, 107, -108, -127, 102, 101, -37, 17, -81, -51, 39, 86, -107, 2, -55, -18, 38, -15, 125, -117, 86, -57, -55, 67, 83, -66, -112, -107, -84, -96, -17, 83, -46, -38, 18, 113, 9, -5, 6, -90, -21, -114, 41, 92, -42, 119, 90, 110, -121, 87, 96, -108, -92, 108, 59, -79, 43, -74, -59, 47, 1, 10, -83, -56, 121, -60, -103, 23, -50, -25, 110, 28, -5, -14, 96, -71, 7, 90, -79, -39, 38, -108, -22, -92, 109, 46, 39, 100, 26, -5, -8, 77, 2, 3, 1, 0, 1]
         * private key:[48, -126, 2, 118, 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 4, -126, 2, 96, 48, -126, 2, 92, 2, 1, 0, 2, -127, -127, 0, -123, 110, -105, -87, 112, 110, -77, -79, 95, -92, 61, -101, 49, -34, 123, 20, 32, -6, -31, -90, 126, -15, 7, -89, 62, 112, -14, 57, -66, -35, -45, -111, -90, 33, 72, -119, -102, 107, -108, -127, 102, 101, -37, 17, -81, -51, 39, 86, -107, 2, -55, -18, 38, -15, 125, -117, 86, -57, -55, 67, 83, -66, -112, -107, -84, -96, -17, 83, -46, -38, 18, 113, 9, -5, 6, -90, -21, -114, 41, 92, -42, 119, 90, 110, -121, 87, 96, -108, -92, 108, 59, -79, 43, -74, -59, 47, 1, 10, -83, -56, 121, -60, -103, 23, -50, -25, 110, 28, -5, -14, 96, -71, 7, 90, -79, -39, 38, -108, -22, -92, 109, 46, 39, 100, 26, -5, -8, 77, 2, 3, 1, 0, 1, 2, -127, -128, 42, 65, -70, -16, 35, 54, 59, -36, 57, -112, -41, 29, -42, 46, 66, 70, 62, -89, -107, 92, -40, 40, -41, 38, -91, 4, -70, 1, -123, -85, -44, -69, -12, -93, -25, -56, 4, 74, 123, -82, 123, 82, 88, -115, -126, 98, 115, 73, -35, 73, 19, -48, 67, 47, 9, 118, 94, 86, 73, 43, -12, 82, -114, -111, 18, 84, 71, 56, -27, 38, 110, 112, 68, -27, -34, 3, -20, 106, -50, -65, 14, -23, 103, 96, -117, 103, 94, -16, -22, 94, -95, -27, -124, 79, 105, 113, -117, 67, 36, -119, -53, 88, 50, 85, 102, -87, -13, -88, 46, 2, -19, -128, 50, -11, 124, -39, 114, -8, -92, 45, -56, 60, 77, 79, 27, -14, 1, 33, 2, 65, 0, -49, 67, -91, -2, 112, 18, 22, -99, -30, 107, 31, 14, 72, -123, -56, 50, -95, 81, -68, -122, -53, 99, -41, 13, -127, -1, 19, -51, 114, -1, 53, 66, 53, 126, -115, 73, -20, 77, 94, 42, -120, -37, -51, -105, -51, -112, 21, 8, -40, -22, 83, -118, 126, 101, -85, 4, -57, 124, -30, -56, 31, 100, -115, 117, 2, 65, 0, -92, -50, -107, 79, -88, 122, -115, -75, 10, -53, -13, 17, -35, -71, 23, -105, -117, 50, -68, -82, 28, 85, 75, -108, -62, -104, 6, -34, 90, -126, 8, -73, 81, 30, -59, -42, 99, 28, 14, -11, -78, -79, -47, 4, 115, -115, -57, -59, 62, 43, 76, 89, 99, -35, 3, 109, 56, 28, -32, -12, 109, -15, 44, 121, 2, 64, 14, 122, 105, 68, 6, -52, 28, -84, 86, -66, -88, -30, -76, -118, 51, -37, -27, -116, -14, 32, 112, 96, -65, 11, 0, -125, -78, -4, 109, 6, 10, -48, 95, 48, 65, -22, 98, 9, 93, -124, -105, 30, -45, -109, -63, 22, 73, -127, -34, -75, 47, -76, -62, -13, -109, 40, -72, 78, -81, 0, -49, -64, -108, 105, 2, 65, 0, -93, 8, 21, 94, 98, -67, 49, 119, -103, -63, -99, -89, -37, -35, -96, -1, -15, -36, 116, 24, 12, -63, 55, -18, 101, -3, -68, 53, -77, -11, 20, -88, 59, -96, 36, 119, 107, 61, -77, 114, -52, -99, -24, -75, -19, -107, -96, -68, 119, 62, -97, -35, -127, -45, 125, -13, 74, 98, 15, 10, -35, -61, -119, -119, 2, 64, 55, -102, -54, 125, 65, -92, 61, -109, -25, -87, 11, -125, -33, -3, -123, -119, 24, 127, 109, -56, -102, -76, -127, -38, 38, -111, 72, 33, -122, -121, 72, 4, -31, -66, -107, -97, -122, 48, -81, 101, 83, 127, -3, -125, 0, -94, -46, 3, 25, -64, 74, -34, 113, -55, 97, -118, 122, -12, 91, 127, -79, -124, 34, 2]
         * [28, 0, -115, -123, 118, -4, -76, 45, 17, -76, 106, 71, 1, -122, -56, 93, -28, 1, -80, -52, 4, -21, 65, 124, 96, -24, 94, -62, 5, -128, -2, 95, 6, -41, -22, -62, 109, -71, -21, -95, -113, -72, -29, 18, 17, -70, -22, 64, 107, 65, -4, -120, -4, -2, 82, -4, 85, 13, 46, 57, -75, 1, 113, 74, -101, -102, 106, -85, 29, -117, 102, -122, -50, -1, -102, -113, 109, -28, -20, -101, -113, -40, -114, -46, -53, 120, 69, -107, -108, 38, -121, 7, 46, -125, -101, -96, -92, -94, -106, 82, -75, -26, 27, -111, 120, -50, 122, -120, -125, 47, -33, 44, 88, 113, 127, 27, -86, -124, 26, -21, -44, 24, 110, 25, -10, 13, -37, 113]
         * 明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这
         * [122, 31, -8, -45, 75, -50, 40, 57, 55, 114, 2, -60, 25, 74, -11, 69, -56, -73, -22, 48, 75, -107, 68, 19, 125, -34, -17, -10, 68, 104, 116, -71, 125, -37, 50, -102, 93, 71, 18, 4, -4, 82, -13, -61, -105, 44, -17, 23, 15, -8, -78, 112, -12, 59, 126, 100, 6, 47, -78, 28, -100, 52, 115, -84, -72, -100, 70, -60, 19, -48, 118, 89, 66, 69, 96, 67, 22, -101, -56, -8, 8, 116, -45, 71, -98, -37, 79, 59, 91, 59, 62, -10, 114, 110, 105, -107, -1, -11, 0, 81, -58, 33, 25, 41, 101, 108, 18, 70, -43, -69, -47, 79, -47, 124, 51, -20, -16, -47, -25, -47, 71, 1, -96, -25, -72, -123, 6, -120]
         * 明文长度(bytes) <= 密钥长度(bytes)-11；密文长度等于密钥长度.当然这
         */
    }

}