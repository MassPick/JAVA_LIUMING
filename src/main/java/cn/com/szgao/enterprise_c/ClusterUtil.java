package cn.com.szgao.enterprise_c;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

public class ClusterUtil {

	private  static final Logger log = LogManager.getLogger(ClusterUtil.class);
	//连接服务器
	private  static Cluster cluster = null;
	//桶对象
	private static Bucket bucket = null;
	public static synchronized Bucket commonBucket(String bucketName){
		if(null==cluster){
			log.info("初始化:cluster连接服务器");
			cluster = CouchbaseCluster.create("192.168.0.253");
		}
		//连接指定的桶
		bucket = cluster.openBucket(bucketName);		
		return bucket;
	}
}
