package timeline.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import timeline.accessor.PostAccessor;
import timeline.model.Message;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.mapping.Result;

@Service
public class SampleService {
	@Autowired
	private Cluster cluster;
	@Autowired
	private PostAccessor postAccessor;
	
	public void registPost(PostForm form){
		registPost(form.getUserId(), form.getContent());
	}
	
	public void registPost(String userid, String content){
			ResultSet results = postAccessor.selectTimeUuid();
			UUID messageid = results.one().getUUID("timeuuid");
			postAccessor.insertPost(userid, messageid, new Date().getTime());
			postAccessor.insertMessage(messageid, content);
			postAccessor.insertTimeLine(userid, messageid, userid);
			for(Row row : postAccessor.selectFollowerList(userid)){
				postAccessor.insertTimeLine(row.getString("userid"), messageid, userid);
			}
	}
	
	public List<Post> getTimeLine(String userId){
		List<Post> postList = new ArrayList<>();
		
		Result<Message> results = postAccessor.selectTimeLine(userId);
		for(Message message : results){
			String content = getContent(postAccessor, message.getMessageId());
			if(!StringUtils.isBlank(content)){
				Post post = new Post();
				post.setContent(content);
				post.setPostDate(message.getPostDate());
				post.setUserId(message.getOwnerId());
				postList.add(post);
			}
		}
		return postList;
	}

	
	public List<Post> getPostList(String userId){
		List<Post> postList = new ArrayList<>();
		
		Result<Message> results = postAccessor.selectPostList(userId);
		for(Message message : results){
			String content = getContent(postAccessor, message.getMessageId());
			if(!StringUtils.isBlank(content)){
				Post post = new Post();
				post.setContent(content);
				post.setPostDate(message.getPostDate());
				postList.add(post);
			}
		}
		return postList;
	}
	
	private String getContent(PostAccessor test, UUID messageId){
		ResultSet result = test.selectMessage(messageId);
		Row row = result.one();
		
		if(row != null){
			return row.getString("content");
		}else{
			return null;
		}
	}
	
	public void addFollow(String userId, String followId){
		postAccessor.insertFollow(userId, followId);
		Result<Message> results = postAccessor.selectPostList(followId);
		for(Message msg : results){
			postAccessor.insertTimeLine(userId, msg.getMessageId(), followId);
		}
	}
	
	public void releaseFollow(String userId, String followId){
		postAccessor.deleteFollow(userId, followId);
		Result<Message> results = postAccessor.selectPostList(followId);
		for(Message msg : results){
			postAccessor.deleteTimeLine(userId, msg.getMessageId());
		}
	}

	public List<String> getFollowList(String userid) {
		List<String> followList = new ArrayList<>();
		for(Row row : postAccessor.selectFollowList(userid)){
			followList.add(row.getString("followid"));
		}
		return followList;
	}

	public Object getFollowerList(String userid) {
		List<String> followerList = new ArrayList<>();
		for(Row row : postAccessor.selectFollowerList(userid)){
			followerList.add(row.getString("userid"));
		}
		return followerList;
	}
}
