package nl.ivonet.springframework.context.support;

import java.util.Locale;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class DatabaseMessageSourceTest extends AbstractDependencyInjectionSpringContextTests {
	
	private DatabaseMessageSource messageSource;
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setMessageSource(DatabaseMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[]{"nl/ivonet/springframework/context/support/applicationContext.xml" };
	}
	

	
	public void testDatabaseMessageSource() {
		/* Testdata samenstellen */
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			jdbcTemplate.execute("insert into messages(id, item) values('test1', 'message1')");
			jdbcTemplate.execute("insert into messages(id, item) values('test2', 'message {0} en nog {1}')");
		} catch (DataIntegrityViolationException e) {
			//pass
		}
		
		assertEquals("message1" , messageSource.getMessage("test1",null, Locale.UK));
		assertEquals("message ivo en nog woltring" , messageSource.getMessage("test2",new String[]{"ivo","woltring"}, Locale.UK));
		assertEquals("message ivo en nog woltring" , messageSource.getMessage("test2",new String[]{"ivo","woltring"}, Locale.UK));
		assertEquals("message ivo en nog woltring" , messageSource.getMessage("test2",new String[]{"ivo","woltring"}, Locale.UK));
		messageSource.setUseCodeAsDefaultMessage(true);
		assertEquals("notexistingmessage", messageSource.getMessage("notexistingmessage",null,Locale.UK));
	}
	
}
