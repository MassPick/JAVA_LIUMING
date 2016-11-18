package cn.com.szgao.util;

import java.util.concurrent.TimeUnit;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.google.gson.Gson;

public class CouchbaseUtils {

	static Cluster cluster = null;
	// 桶对象
	static Bucket bucket = null;

	// public static synchronized Bucket commonBucket(String bucketName){
	// if(null==cluster)
	// cluster = CouchbaseCluster.create("192.168.1.3");
	// //连接指定的桶
	// bucket = cluster.openBucket(bucketName);
	// return bucket;
	// }
	public static synchronized Bucket commonBucket(String ip, String bucketName) {
		if (null == cluster)
			cluster = CouchbaseCluster.create(ip);
		// 连接指定的桶
		bucket = cluster.openBucket(bucketName);
		return bucket;
	}

	public static JsonDocument getDocById(String ip, String bucketName, String key) {
		if (null == cluster)
			cluster = CouchbaseCluster.create(ip);
		// 连接指定的桶
		bucket = cluster.openBucket(bucketName);

		JsonDocument queryDoc = null;
		queryDoc = bucket.get(key, 60, TimeUnit.MINUTES);
		if (null == queryDoc) {
			return null;
		}
		return queryDoc;
	}
	
	public static JsonDocument getDocById( String key) {
	 
		JsonDocument queryDoc = null;
		queryDoc = bucket.get(key, 60, TimeUnit.MINUTES);
		if (null == queryDoc) {
			return null;
		}
		return queryDoc;
	}

	/**
	 * @param args
	 * @author liuming
	 * @Date 2015-10-13 上午11:50:49
	 */
	public static void main(String[] args) {

	}
	
	
	
	/**
	 * 桶对象
	 */
	private Bucket bck;
//	private Cluster cluster;
	private JsonObject jsonObject = null;
	private JsonDocument doc = null;
	// json转换工具
	private static Gson GJSON = new Gson();
	public boolean addput(String key, Object obj, String bucket, String pwd, long timeout) {
		try {
			jsonObject = JsonObject.fromJson(GJSON.toJson(obj));
			doc = JsonDocument.create(key, jsonObject);
			bck = cluster.openBucket(bucket, pwd, timeout, TimeUnit.MILLISECONDS);
			bck.upsert(doc, timeout, TimeUnit.MILLISECONDS);
			return true;
		} catch (Exception e) {
//			logger.error("addput", e);
		} finally {
			jsonObject = null;
			doc = null;
			if (null != bck) {
				bck.close();
			}
			bck = null;
		}
		return false;
	}
	

}
