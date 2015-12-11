package timeline.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import timeline.accessor.Test;
import timeline.sample.Account;
import timeline.sample.Address;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

@Service
public class SampleService {
	@Autowired
	private Cluster cluster;
	
	public void sampleRegist(String email){
		
		try(Session session = cluster.connect()){
			Mapper<Account> mapper = new MappingManager(session).mapper(Account.class);
			List<String> phones = new ArrayList<String>();
			phones.add("707-555-3537");
			Address address = new Address("25800 Arnold Drive", "Sonoma", 95476, phones);
			Account account = new Account("John Doe", email, address);
			mapper.save(account);
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
}
