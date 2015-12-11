package timeline.sample;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class Main {
	private static Cluster cluster;

	public static void main(String[] args) {
		Mapper<Account> mapper = new MappingManager(getSession()).mapper(Account.class);
		Phone phone = new Phone("home", "707-555-3537");
		List<String> phones = new ArrayList<String>();
		phones.add("707-555-3537");
		Address address = new Address("25800 Arnold Drive", "Sonoma", 95476, phones);
		Account account = new Account("John Doe", "jd@example.com", address);
		mapper.save(account);
		Account whose = mapper.get("jd@example.com");
		System.out.println("Account name: " + whose.getName());
		mapper.delete(account);
	}

	private static Session getSession() {
		cluster = Cluster.builder().addContactPoints("192.168.33.12", "192.168.33.13").build();
//		Metadata metadata = cluster.getMetadata();
//		System.out.printf("Connected to cluster: %s\n",
//				metadata.getClusterName());
//		for (Host host : metadata.getAllHosts()) {
//			System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
//					host.getDatacenter(), host.getAddress(), host.getRack());
//		}
		return cluster.connect();
	}

}
