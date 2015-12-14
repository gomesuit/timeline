package timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import timeline.accessor.PostAccessor;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.mapping.MappingManager;

@SpringBootApplication
public class ApplicationStarter {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationStarter.class, args);
	}
	
	@Bean
	public Cluster cluster(){
		Cluster cluster = Cluster.builder()
				.addContactPoints("192.168.33.12", "192.168.33.13")
				.build();
		
		return cluster;
	}
	
	@Bean
	@Autowired
	public PostAccessor postAccessor(Cluster cluster){
		MappingManager manager = new MappingManager(cluster.connect());
		return manager.createAccessor(PostAccessor.class);
	}
}
