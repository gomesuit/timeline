package timeline.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import timeline.accessor.PostAccessor;
import timeline.model.Message;
import timeline.sample.Account;
import timeline.sample.Address;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

@Service
public class SampleService {
	@Autowired
	private Cluster cluster;
	
	public void sampleRegist(String email){
		
		try(Session session = cluster.connect()){
			List<String> phones = new ArrayList<String>();
			phones.add("707-555-3537");
			Address address = new Address("25800 Arnold Drive", "Sonoma", 95476, phones);
			
//			Mapper<Account> mapper = new MappingManager(session).mapper(Account.class);
//			Account account = new Account("John Doe", email, address);
//			mapper.save(account);
			

			MappingManager manager = new MappingManager (session);
			PostAccessor test = manager.createAccessor(PostAccessor.class);
			test.insertAccount(email, "John Doe", address);
		}
	}

	public Account get(String userid) {
		try(Session session = cluster.connect()){
			MappingManager manager = new MappingManager (session);
			PostAccessor test = manager.createAccessor(PostAccessor.class);
			Account whose = test.getAccount(userid);
			return whose;
		}
	}
	
	public void registPost(PostForm form){
		registPost(form.getUserId(), form.getContent());
	}
	
	public void registPost(String userid, String content){
		try(Session session = cluster.connect()){
			PostAccessor test = createAccessor(session);
			ResultSet results = test.selectTimeUuid();
			UUID messageid = results.one().getUUID("timeuuid");
			test.insertPost(userid, messageid);
			test.insertMessage(messageid, content);
			test.insertTimeLine(userid, messageid, userid);
			for(Row row : test.selectFollowerList(userid)){
				test.insertTimeLine(row.getString("userid"), messageid, userid);
			}
		}
	}
	
	public List<Post> getTimeLine(String userId){
		List<Post> postList = new ArrayList<>();
		
		try(Session session = cluster.connect()){
			PostAccessor test = createAccessor(session);
			Result<Message> results = test.selectTimeLine(userId);
			for(Message message : results){
				String content = getContent(test, message.getMessageId());
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
	}

	
	public List<Post> getPostList(String userId){
		List<Post> postList = new ArrayList<>();
		
		try(Session session = cluster.connect()){
			PostAccessor test = createAccessor(session);
			Result<Message> results = test.selectPostList(userId);
			for(Message message : results){
				String content = getContent(test, message.getMessageId());
				if(!StringUtils.isBlank(content)){
					Post post = new Post();
					post.setContent(content);
					post.setPostDate(message.getPostDate());
					postList.add(post);
				}
			}
			return postList;
		}
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
	
	private PostAccessor createAccessor(Session session){
		MappingManager manager = new MappingManager(session);
		return manager.createAccessor(PostAccessor.class);
	}
	
	public void addFollow(String userId, String followId){
		try(Session session = cluster.connect()){
			PostAccessor accessor = createAccessor(session);
			accessor.insertFollow(userId, followId);
			Result<Message> results = accessor.selectPostList(followId);
			for(Message msg : results){
				accessor.insertTimeLine(userId, msg.getMessageId(), followId);
			}
		}
	}
	
	public void releaseFollow(String userId, String followId){
		try(Session session = cluster.connect()){
			PostAccessor accessor = createAccessor(session);
			accessor.deleteFollow(userId, followId);
			Result<Message> results = accessor.selectPostList(followId);
			for(Message msg : results){
				accessor.deleteTimeLine(userId, msg.getMessageId());
			}
		}
		
	}

	public List<String> getFollowList(String userid) {
		List<String> followList = new ArrayList<>();
		try(Session session = cluster.connect()){
			PostAccessor accessor = createAccessor(session);
			for(Row row : accessor.selectFollowList(userid)){
				followList.add(row.getString("followid"));
			}
			
		}
		return followList;
	}

	public Object getFollowerList(String userid) {
		List<String> followerList = new ArrayList<>();
		try(Session session = cluster.connect()){
			PostAccessor accessor = createAccessor(session);
			for(Row row : accessor.selectFollowerList(userid)){
				followerList.add(row.getString("userid"));
			}
		}
		return followerList;
	}
}
