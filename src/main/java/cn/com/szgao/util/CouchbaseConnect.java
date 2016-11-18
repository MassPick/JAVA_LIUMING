package cn.com.szgao.util;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
public class CouchbaseConnect {
	
	static Cluster cluster = null;
	//桶对象
	static Bucket bucket = null;
//	public static synchronized Bucket commonBucket(String bucketName){
//		if(null==cluster)
//		cluster = CouchbaseCluster.create("192.168.1.3");
//		//连接指定的桶
//		bucket = cluster.openBucket(bucketName);		
//		return bucket;
//	}
	public static synchronized Bucket commonBucket(String ip,String bucketName){
		if(null==cluster)
		cluster = CouchbaseCluster.create(ip);
		//连接指定的桶
		bucket = cluster.openBucket(bucketName);		
		return bucket;
	}

	/**
	 * @param args
	 * @author liuming
	 * @Date 2015-10-13 上午11:50:49 
	 */
	public static void main(String[] args) {
		
	}

}
