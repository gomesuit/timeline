package timeline.accessor;

import java.util.UUID;

import timeline.model.Message;
import timeline.sample.Account;
import timeline.sample.Address;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface PostAccessor {
//	@Query("SELECT * FROM complex.users WHERE id = :id")
//	String getUserNamed(@Param("userId") UUID id);

	@Query("SELECT * FROM complex.accounts WHERE email = :email")
	Account getAccount(@Param("email") String email);
	
	@Query("INSERT INTO complex.accounts (email, name, addr) VALUES (:email, :name, :addr)")
	void insertAccount(@Param("email") String email, @Param("name") String name, @Param("addr") Address addr);

//	@Query("INSERT INTO complex.accounts (email, name, addr) VALUES (:account.email, :account.name, :account.address)")
//	void insertAccount(@Param("account") Account account);

	@Query("SELECT uuid() AS uuid FROM system.local")
	ResultSet selectUuid();
	
	@Query("SELECT now() AS timeuuid FROM system.local")
	ResultSet selectTimeUuid();
	
	
	
	@Query("INSERT INTO timeline.post (userid, messageid) VALUES (:userid, :messageid)")
	void insertPost(@Param("userid") String userid, @Param("messageid") UUID messageid);

	@Query("SELECT messageid, dateof(messageid) AS postDate FROM timeline.post WHERE userid = :userid ORDER BY messageid DESC")
	Result<Message> selectPostList(@Param("userId") String userId);

	
	
	@Query("INSERT INTO timeline.message (messageid, content) VALUES (:messageid, :content)")
	void insertMessage(@Param("messageid") UUID messageid, @Param("content") String content);

	@Query("SELECT content FROM timeline.message WHERE messageid = :messageid")
	ResultSet selectMessage(@Param("messageid") UUID messageid);
	
	
	
	@Query("INSERT INTO timeline.timeline (userid, messageid, ownerid) VALUES (:userid, :messageid, :ownerid)")
	void insertTimeLine(@Param("userid") String userid, @Param("messageid") UUID messageid, @Param("ownerid") String ownerid);

	@Query("SELECT messageid, dateof(messageid) AS postDate, userid, ownerid FROM timeline.timeline WHERE userid = :userid ORDER BY messageid DESC")
	Result<Message> selectTimeLine(@Param("userid") String userid);
	
	@Query("DELETE FROM timeline.timeline WHERE userid = :userid AND messageid = :messageid")
	void deleteTimeLine(@Param("userid") String userid, @Param("messageid") UUID messageid);
	
	
	

	@Query("INSERT INTO timeline.follow (userid, followid) VALUES (:userid, :followid)")
	void insertFollow(@Param("userid") String userId, @Param("followid") String followId);
	@Query("DELETE FROM timeline.follow WHERE userid = :userid AND followid = :followid")
	void deleteFollow(@Param("userid") String userId, @Param("followid") String followId);
	@Query("SELECT followid FROM timeline.follow WHERE userid = :userid")
	ResultSet selectFollowList(@Param("userid") String userId);
	@Query("SELECT userid FROM timeline.follow WHERE followid = :followid")
	ResultSet selectFollowerList(@Param("followid") String userId);
	
}
