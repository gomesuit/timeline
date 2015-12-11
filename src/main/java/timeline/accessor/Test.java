package timeline.accessor;

import timeline.sample.Account;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface Test {
//	@Query("SELECT * FROM complex.users WHERE id = :id")
//	String getUserNamed(@Param("userId") UUID id);

	@Query("SELECT * FROM complex.accounts WHERE email = :email")
	Account getAccount(@Param("email") String email);
}
