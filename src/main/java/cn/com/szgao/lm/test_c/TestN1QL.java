package cn.com.szgao.lm.test_c;

import com.couchbase.client.java.Bucket;

import cn.com.szgao.util.CouchbaseConnect;

public class TestN1QL {
	static Bucket bucket=null;

	/**
	 * @param args
	 * @author liuming
	 * @Date 2015-10-26 上午11:43:43 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		while (true) {
			try {
				bucket=CouchbaseConnect.commonBucket("192.168.0.30:8091", "executedNew");
				break;
			} catch (Exception e) {
//				log.info("---------------------------> 插入BC超时");
//				log.error(e.getMessage());
			}
		}
		
//		N1qlQueryResult queryResult =
//				   bucket.query(N1qlQuery.simple("SELECT * FROM beer-sample LIMIT 10"));
		
//		Index.createPrimaryIndex().on("executedNew");
		
//		bucket.async()
//		.query(select("*").from("beer-sample").limit(10))
//		.subscribe(result -> {
//			result.errors()
//				.subscribe(
//					e -> System.err.println("N1QL Error/Warning: " + e),
//					runtimeError -> runtimeError.printStackTrace()
//				);
//			result.rows()
//				.map(row -> row.value())
//				.subscribe(
//					rowContent -> System.out.println(rowContent),
//					runtimeError -> runtimeError.printStackTrace()
//				)
//			}
//		);
		
		
	}

}
