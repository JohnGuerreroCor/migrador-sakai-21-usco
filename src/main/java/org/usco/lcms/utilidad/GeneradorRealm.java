package org.usco.lcms.utilidad;

public class GeneradorRealm {

	public static String generateRealmId() {
		StringBuffer sb = new StringBuffer();
		sb.append(Long.toHexString((long) (Math.random() * 100000000000.0)).substring(0, 8)).append("-")
				.append(Long.toHexString((long) (Math.random() * 100000000.0)).substring(0, 4)).append("-")
				.append(Long.toHexString((long) (Math.random() * 100000000.0)).substring(0, 4)).append("-")
				.append(Long.toHexString((long) (Math.random() * 100000000.0)).substring(0, 4)).append("-")
				.append(Long.toHexString((long) (Math.random() * 1000000000000000000.0)).substring(0, 12));
		return sb.toString();

	}
}