package timeline.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import timeline.accessor.Test;
import timeline.sample.Account;
import timeline.sample.Address;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
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
			Test test = manager.createAccessor(Test.class);
			test.insertAccount(email, "John Doe", address);
		}
	}

	public Account get(String userid) {
		try(Session session = cluster.connect()){
			MappingManager manager = new MappingManager (session);
			Test test = manager.createAccessor(Test.class);
			Account whose = test.getAccount(userid);
			return whose;
		}
	}
	
	public void registPost(String userid, String content){
		try(Session session = cluster.connect()){
			MappingManager manager = new MappingManager (session);
			Test test = manager.createAccessor(Test.class);
			ResultSet a = test.selectTimeUuid();
			UUID messageid = a.one().getUUID("timeuuid");
			test.insertPost(userid, messageid);
			test.insertMessage(messageid, content);
		}
	}
}
