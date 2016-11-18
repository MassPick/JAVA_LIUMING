package cn.com.szgao.timer;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

public class Test2 {

	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
			+ "AccountKey=O1x8o0Mr/TrSVwU62cQV5nFOi9h58ELMDlhxsQjmaMHj8RLbeAFitaS0qmELri7XXdUogqds1kR1QxclE+CCOA==;"
			+ "EndpointSuffix=core.chinacloudapi.cn";

	public static void main(String[] args) {
		// retrieve();
		retrieve2();
		// updateFirst();
		// countLen();

		// deleteMessage();
		// deleteMessageMulti();
		// listQueue();
		// deleteQueue();

		System.out.println("完成");
	}

	public static void retrieve2() {

		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the queue client.
			CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

			// Retrieve a reference to a queue.
			CloudQueue queue = queueClient.getQueueReference("zhixing");
			// CloudQueueMessage retrievedMessage = queue.peekMessage();
			CloudQueueMessage retrievedMessage = queue.retrieveMessage();/// ----------------------异常
			String retrieve_id = retrievedMessage.getId();
			System.out.println(retrieve_id + "  " + retrievedMessage.getMessageContentAsString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
