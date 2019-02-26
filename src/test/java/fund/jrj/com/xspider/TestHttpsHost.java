package fund.jrj.com.xspider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;

import fund.jrj.com.xspider.bo.HostCertificate;
import fund.jrj.com.xspider.dao.HostCertificateDao;
import fund.jrj.com.xspider.utils.DBUtils;

/**
 * @author 18272
 * 证书过期定时任务校验
 */
public class TestHttpsHost {

	public static void main(String[] args) throws IOException {
		File file=new File(TestHttpsHost.class.getResource("").getPath()+"hosts.txt");
		List<String> hosts=FileUtils.readLines(file,"utf-8");
		List<HostCertificate> result=new LinkedList<>();
		for(String host:hosts) {
			X509Certificate cert=getCertExpired(host);
			if(cert==null) {
				continue;
			}
			HostCertificate c=new HostCertificate();
			c.setEndTime(cert.getNotAfter());
			c.setHost(host);
			c.setIssueDn(cert.getIssuerDN().getName());
			c.setPrivateKey(cert.getSignature().toString());
			c.setPublicKey(cert.getPublicKey().toString());
			c.setSerialNumber(cert.getSerialNumber().toString());
			c.setStartTime(cert.getNotBefore());
			c.setSubjectDn(cert.getSubjectDN().getName());
			c.setVersion(""+cert.getVersion());
			result.add(c);
		}
		HostCertificateDao hcDao=DBUtils.getInstance().create(HostCertificateDao.class);
		hcDao.add(result);
	}

	public static X509Certificate getCertExpired(String httpsUrl) {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new TestHttpsHost().new NullHostNameVerifier());
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			URL url = new URL(httpsUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(5000);
			conn.connect();
			Certificate[] certificates= conn.getServerCertificates();    
			X509Certificate x509Certificate = (X509Certificate)certificates[0];    
			conn.disconnect();
			return x509Certificate;
			//	        System.out.println("证书版本:" + x509Certificate.getVersion());
			//	        System.out.println("证书编号:" + x509Certificate.getSerialNumber());
			//	        System.out.println("颁发机构:" + x509Certificate.getSubjectDN().getName());
			//	        System.out.println("颁发者:" + x509Certificate.getIssuerDN().getName());
			//	        System.out.println("证书开始时间:" + x509Certificate.getNotBefore());
			//	        System.out.println("有效期止" + x509Certificate.getNotAfter());
			//	        System.out.println("签名算法:" + x509Certificate.getSigAlgName());
			//	        System.out.println("证书公钥:" + x509Certificate.getPublicKey());
			//	        System.out.println("证书签名:" + x509Certificate.getSignature());

		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	} };
	public class NullHostNameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}


}