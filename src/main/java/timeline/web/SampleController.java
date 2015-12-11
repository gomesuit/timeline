package timeline.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import timeline.accessor.Test;
import timeline.sample.Account;
import timeline.sample.Address;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

@Controller
public class SampleController {
	@Autowired
	private Cluster cluster;
	
    @RequestMapping("/")
    String sample(Model model) throws Exception {
    	
        return "sample";
    }

    @RequestMapping("/{userid}")
    @ResponseBody
    public Account user(@PathVariable String userid, Model model) throws Exception {
    	add(userid);
    	Account account = get(userid);
    	
        return account;
    }
    
	private Account get(String userid) {
		MappingManager manager = new MappingManager (cluster.connect());
		Test test = manager.createAccessor(Test.class);
		Account whose = test.getAccount(userid);
		return whose;
	}

	public void add(String email) {
		Mapper<Account> mapper = new MappingManager(cluster.connect()).mapper(Account.class);
		List<String> phones = new ArrayList<String>();
		phones.add("707-555-3537");
		Address address = new Address("25800 Arnold Drive", "Sonoma", 95476, phones);
		Account account = new Account("John Doe", email, address);
		mapper.save(account);
	}
}
