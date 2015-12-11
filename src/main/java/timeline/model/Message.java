package timeline.model;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "timeline", name = "message")
public class Message {
	@PartitionKey
	@Column(name = "messageid")
	private UUID messageId;
	private Date postDate;
	@Column(name = "userid")
	private String userId;
	
	public UUID getMessageId() {
		return messageId;
	}
	public void setMessageId(UUID messageId) {
		this.messageId = messageId;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
