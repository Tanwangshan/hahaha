package Server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPGetter {
	public static String getLocalIp() {
		InetAddress inetAddress = null;
		boolean isFind = false; 
		Enumeration<NetworkInterface> networkInterfaceLists = null;
		try {
			
			networkInterfaceLists = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (networkInterfaceLists.hasMoreElements()) {
			NetworkInterface networkInterface = (NetworkInterface) networkInterfaceLists.nextElement();
			Enumeration<InetAddress> ips = networkInterface.getInetAddresses();
			
			while (ips.hasMoreElements()) {
				inetAddress = (InetAddress) ips.nextElement();
				if (inetAddress instanceof Inet4Address && inetAddress.isSiteLocalAddress()
						&& !inetAddress.isLoopbackAddress()) {
					isFind = true;
					break;
				}
			}
			if (isFind) {
				break;
			}
		}
		return inetAddress == null ? "" : inetAddress.getHostAddress();
	}
 
}
