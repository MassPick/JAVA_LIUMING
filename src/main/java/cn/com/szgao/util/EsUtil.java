package cn.com.szgao.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class EsUtil {
	public static Client getClient() {

		Client client = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("cluster.name", "masspick");           //masspick2  10                           masspick  9
		map.put("index.refresh_interval", "-1s");
		map.put("client", "true");
		map.put("data", "false");
		map.put("client.transport.ignore_cluster_name", "true");
		map.put("client.transport.nodes_sampler_interval", "10s");
		map.put("client.transport.ping_timeout", "30s");
		Settings settings = Settings.settingsBuilder().put(map).build();
		try {
			client = TransportClient.builder().settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.9"), 9300));
			System.out.println("---------------->");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;

	}
}
