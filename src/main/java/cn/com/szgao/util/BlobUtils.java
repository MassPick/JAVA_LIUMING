package cn.com.szgao.util;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

public class BlobUtils {
	
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
			+ "AccountKey=O1x8o0Mr/TrSVwU62cQV5nFOi9h58ELMDlhxsQjmaMHj8RLbeAFitaS0qmELri7XXdUogqds1kR1QxclE+CCOA==;"
			+ "EndpointSuffix=core.chinacloudapi.cn";
	
	public static CloudQueueClient queueClient =null;
	
	static{
		CloudStorageAccount storageAccount = null;
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	 queueClient = storageAccount.createCloudQueueClient();
	}
	
	/**
	 * 通过调用 peekMessage，你可以扫视队列前面的消息，而不会从队列中删除它。
	 * 
	 * @Description: TODO
	 * @return void
	 * @author liuming
	 * @date 2016年5月3日 下午5:19:52
	 */
	public static void lastNext(String queueName) {
		try {
			// Retrieve a reference to a queue.
			CloudQueue queue = queueClient.getQueueReference(queueName);
			// Peek at the next message.
			CloudQueueMessage peekedMessage = queue.peekMessage();

			while (null != peekedMessage) {
				// Output the message value.
				if (peekedMessage != null) {
					System.out.println(peekedMessage.getMessageContentAsString());
//					peekedMessage = queue.peekMessage();
				}
			}

		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
	}
}
