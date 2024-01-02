package Y2019.M02.D11_cert;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

/**
 * @author Bob.Zhu
 * @Email adolphor@qq.com
 * @since v0.0.5
 */
public class CertUtils {

  public static final String caAlias = "mynety-cert";
  public static final char[] caPassword = "mynety-ca-password".toCharArray();

  private static final String preSubject = "L=HangZhou, ST=ZheJiang, C=CN, O=adolphor@qq.com, OU=https://github.com/adolphor/mynety, owner=Bob.Zhu, CN=";
  private static final String signatureAlgorithm = "SHA256WithRSAEncryption";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }


  /**
   * Generate all kinds of ca root cert
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    final String caCnName = "mynety Root CA";

    Date notBefore = today();
    Date notAfter = addYears(notBefore, 100);
    KeyPair keyPair = CertUtils.genKeyPair();
    String subject = preSubject + caCnName;
    X509Certificate caCert = CertUtils.genCACert(subject, notBefore, notAfter, keyPair);

    final String filePath = "/Users/liangwang/IdeaProjects/adolphor/target/";

    // save cert to file
    String outName = filePath + "mynety-root-ca.crt";
    saveCertToFile(caCert.getEncoded(), outName);
    // save private key to file
    outName = filePath + "mynety-root-ca-private-key.der";
    saveCertToFile(keyPair.getPrivate().getEncoded(), outName);

    // save keyStore as jks file
    String storeType = KeyStore.getDefaultType();
    outName = filePath + "mynety-root-ca.jks";
    saveKeyStoreToFile(caCert, storeType, keyPair, outName);
    // save keyStore as pkcs base64 file
    storeType = KeyStore.getDefaultType();
    outName = filePath + "mynety-root-ca-jks-base64.txt";
    saveKeyStoreToFile(caCert, storeType, keyPair, outName);
    // save keyStore as pkcs12 file
    storeType = "PKCS12";
    outName = filePath + "mynety-root-ca.p12";
    saveKeyStoreToFile(caCert, storeType, keyPair, outName);
    // save keyStore as pkcs base64 file
    storeType = "PKCS12";
    outName = filePath + "mynety-root-ca-pkcs12-base64.txt";
    saveKeyStoreToFile(caCert, storeType, keyPair, outName);
  }

  public static PrivateKey loadPriKey(String filePath, String password)
    throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
    KeyStore keyStore = loadKeyStore(filePath, password);
    Enumeration<String> aliasesEnum = keyStore.aliases();
    while (aliasesEnum.hasMoreElements()) {
      String aliases = aliasesEnum.nextElement();
      return (PrivateKey) keyStore.getKey(aliases, password.toCharArray());
    }
    throw new IllegalArgumentException("configuration of caKeyStoreFile is NOT right!");
  }

  public static X509Certificate loadCert(String filePath, String password)
    throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
    KeyStore keyStore = loadKeyStore(filePath, password);
    Enumeration<String> aliasesEnum = keyStore.aliases();
    while (aliasesEnum.hasMoreElements()) {
      String aliases = aliasesEnum.nextElement();
      return (X509Certificate) keyStore.getCertificate(aliases);
    }
    throw new IllegalArgumentException("configuration of caKeyStoreFile is NOT right!");
  }

  public static X509Certificate readCert(String filePath) throws CertificateException, FileNotFoundException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    FileInputStream inputStream = new FileInputStream(filePath);
    return (X509Certificate) cf.generateCertificate(inputStream);
  }

  /**
   * generate MITM cert by CA
   */
  public static X509Certificate genMitmCert(String issuer, PrivateKey caPriKey, Date notBefore, Date notAfter, PublicKey publicKey, String... hosts)
    throws CertIOException, CertificateException, OperatorCreationException {
    String subject = preSubject + hosts[0];
    JcaX509v3CertificateBuilder jv3Builder = new JcaX509v3CertificateBuilder(
      new X500Name(RFC4519Style.INSTANCE, issuer),
      BigInteger.valueOf(System.currentTimeMillis() + getRandomInt(1000, 9999)),
      notBefore,
      notAfter,
      new X500Name(RFC4519Style.INSTANCE, subject),
      publicKey
    );
    GeneralName[] generalNames = new GeneralName[hosts.length];
    for (int i = 0; i < hosts.length; i++) {
      generalNames[i] = new GeneralName(GeneralName.dNSName, hosts[i]);
    }
    GeneralNames subjectAltName = new GeneralNames(generalNames);
    jv3Builder.addExtension(Extension.subjectAlternativeName, false, subjectAltName);
    ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).build(caPriKey);
    return new JcaX509CertificateConverter().getCertificate(jv3Builder.build(signer));
  }

  // ************************************* Generate the ca root cert and save *************************************

  /**
   * generate the ca root cert.
   * <p>
   * Note: use RFC4519Style.INSTANCE to get the correct seq of subject elements
   */
  public static X509Certificate genCACert(String subject, Date notBefore, Date notAfter, KeyPair keyPair) throws Exception {
    JcaX509v3CertificateBuilder jv3Builder = new JcaX509v3CertificateBuilder(
      new X500Name(RFC4519Style.INSTANCE, subject),
      BigInteger.valueOf(System.currentTimeMillis() + getRandomInt(1000, 9999)),
      notBefore,
      notAfter,
      new X500Name(RFC4519Style.INSTANCE, subject),
      keyPair.getPublic()
    );
    jv3Builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
    ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());
    return new JcaX509CertificateConverter().getCertificate(jv3Builder.build(signer));
  }

  public static KeyPair genKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
    KeyPairGenerator caKeyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
    caKeyPairGen.initialize(2048, new SecureRandom());
    return caKeyPairGen.genKeyPair();
  }

  public static void saveKeyStoreToFile(Certificate cert, String storeType, KeyPair keyPair, String fileName)
    throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, SignatureException {
    KeyStore store = KeyStore.getInstance(storeType);
    store.load(null, null);
    store.setKeyEntry(caAlias, keyPair.getPrivate(), caPassword, new Certificate[]{cert});
    cert.verify(keyPair.getPublic());
    File file = new File(fileName);
    if (file.exists() || file.createNewFile()) {
      if (fileName.endsWith("txt")) {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        store.store(outBuffer, caPassword);
        Files.write(Paths.get(file.toURI()), Base64.encode(outBuffer.toByteArray()));
      } else {
        store.store(new FileOutputStream(file), caPassword);
      }
    }
  }

  public static void saveCertToFile(byte[] encoded, String fileName) throws Exception {
    File file = new File(fileName);
    Files.write(Paths.get(file.toURI()), encoded);
  }

  // ************************************* change cert type *************************************

  public static void jksToPkcs12(String filePath, String password, String type)
    throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, InvalidKeyException, NoSuchProviderException, SignatureException {
    String endName = "p12";
    changeType(filePath, password, type, endName);
  }

  public static void pkcs12ToJks(String filePath, String password, String type)
    throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, InvalidKeyException, NoSuchProviderException, SignatureException {
    String endName = "jks";
    changeType(filePath, password, type, endName);
  }

  private static void changeType(String filePath, String password, String type, String endName) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, InvalidKeyException, NoSuchProviderException, SignatureException {
    X509Certificate cert = loadCert(filePath, password);
    PublicKey publicKey = cert.getPublicKey();
    PrivateKey privateKey = loadPriKey(filePath, password);
    KeyPair keyPair = new KeyPair(publicKey, privateKey);
    filePath = filePath.substring(0, filePath.indexOf("\\.")) + endName;
    saveKeyStoreToFile(cert, type, keyPair, filePath);
  }

  // ************************************* load keyStore *************************************

  /**
   * load keyStore, supported type: jks, jks base64, p12(pkcs12), p12 base64
   */
  private static KeyStore loadKeyStore(String filePath, String password)
    throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
    if (StringUtils.isEmpty(filePath.trim())) {
      throw new IllegalArgumentException("cert keyStore path can NOT be null!");
    }
    String type = getKeyStoreType(filePath);
    KeyStore ks = KeyStore.getInstance(type);
    FileInputStream inputStream;
    try {
      inputStream = new FileInputStream(filePath);
    } catch (FileNotFoundException e) {
      filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + filePath;
      try {
        inputStream = new FileInputStream(filePath);
      } catch (FileNotFoundException e1) {
        throw e1;
      }
    }
    ks.load(inputStream, password.toCharArray());
    return ks;
  }

  private static String getKeyStoreType(String filePath) {
    String type;
    if (filePath.endsWith("p12")) {
      type = "PKCS12";
    } else if (filePath.endsWith("jks")) {
      type = KeyStore.getDefaultType();
    } else if (filePath.endsWith("txt")) {
      if (filePath.endsWith("pkcs12-base64.txt")) {
        type = "PKCS12";
      } else if (filePath.contains("jks-base64.txt")) {
        type = KeyStore.getDefaultType();
      } else {
        throw new IllegalArgumentException("");
      }
    } else {
      throw new IllegalArgumentException("");
    }
    return type;
  }


  public static int getRandomInt(int min, int max) {
    return new Random().nextInt(max - min) + min;
  }


  public static Date today() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  public static Date addYears(Date date, int count) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.YEAR, count);
    return cal.getTime();
  }

}
