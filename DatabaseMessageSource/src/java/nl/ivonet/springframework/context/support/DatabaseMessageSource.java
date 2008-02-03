package nl.ivonet.springframework.context.support;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * {@link org.springframework.context.support.AbstractMessageSource}
 * implementation that access a database table provided by the datasource and
 * basename constructor parameters.
 * 
 * A locale is not important because this implementation supports only 1
 * language. Where one seas a Locale it is only to be compliant to the
 * prescribed interfaces. They will be ignored.
 * 
 * Because it is not easy to detect if a database has been changed without
 * keeping track of the changes with timestamps I am going to implement this
 * refreshing based on a timeout and the refreshing will be done completely
 * without checking if it has actually changed or not. When the timeout has been
 * reached the properties will be updated.
 * 
 * @author Woltring
 */
// TODO errorhandling + threadsafety + reloadability
public class DatabaseMessageSource extends AbstractMessageSource implements ResourceLoaderAware {
	protected final Logger log = Logger.getLogger(getClass());
	private static final String DEFAULT_TABLE = "messages";
	/** The datasource used for accessing the database */
	// private DataSource dataSource;
	/** The name of the table used in the database */
	private String basename;
	/** way to query the db */
	private JdbcTemplate jdbcTemplate;
	/** Cache to hold already loaded properties */
	private final Map<String, String> cachedProperties = new HashMap<String, String>();
	/** Milliseconds to hold the cash */
	private long cacheMillis = -1;
	/** reset everytime the timestamp was changed */
	private long refreshTimestamp = -1;

	/**
	 * The method that does the loading of all the properties from the database
	 * 
	 * @return properties
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> refreshProperties() {
		synchronized (this.cachedProperties) {
			if (log.isDebugEnabled()) {
				log.debug("Refreshing messages");
			}
			List<Map<String, String>> list = (List<Map<String, String>>) jdbcTemplate.queryForList("Select id, item from " + basename);
			cachedProperties.clear();
			for (Map<String, String> map : list) {
				cachedProperties.put(map.get("id"), map.get("item"));
			}
			if (log.isDebugEnabled()) {
				log.debug("Messages: " + cachedProperties);
			}
			refreshTimestamp = System.currentTimeMillis();
			return cachedProperties;
		}
	}

	private Map<String, String> getProperties() {
		synchronized (this.cachedProperties) {
			if (this.cachedProperties != null && (refreshTimestamp < 0 || refreshTimestamp > System.currentTimeMillis() - this.cacheMillis)) {
				// up to date
				return this.cachedProperties;
			}
			return refreshProperties();
		}
	}

	public String getBasename() {
		return basename;
	}

	/**
	 * initializer for preventing dublicate code in the constructors
	 * 
	 * @param dataSource
	 * @param basename
	 */
	private void initialize(DataSource dataSource, String basename) {
		// this.dataSource = dataSource;
		this.basename = basename;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		//setUseCodeAsDefaultMessage(true);
		log.debug(refreshProperties());
		log.debug("DatabaseMessageSource is constructed with tableName: " + basename);
	}

	/**
	 * <p>
	 * This constructor must get the dataSource from the spring
	 * applicationContext.
	 * </p>
	 * <p>
	 * the default table name will be used for accessing the correct table
	 * </p>
	 * 
	 * @param dataSource
	 *            The dataSource used by this class
	 */
	public DatabaseMessageSource(DataSource dataSource) {
		super();
		initialize(dataSource, DEFAULT_TABLE);
	}

	/**
	 * <p>
	 * This constructor provides the possibility of accessing a custom table
	 * from the given datasource.
	 * </p>
	 * 
	 * @param dataSource
	 * @param basename
	 */
	public DatabaseMessageSource(DataSource dataSource, String basename) {
		super();
		initialize(dataSource, basename);
	}

	/**
	 * This method is neede if we want to make use of the abstracht class we are
	 * extending from. The locale argument is given but in this implemention I
	 * will do nothing with it. The website I am currently building is in only 1
	 * language and that is in dutch.
	 */
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Getting message with code: " + code);
		}
		String msg = getProperties().get(code);
		MessageFormat result = createMessageFormat(msg, locale);
		return result;
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		return getProperties().get(code);
	}

	public void setResourceLoader(ResourceLoader arg0) {
	}

	/**
	 * Set the number of seconds to cache loaded properties .
	 * <ul>
	 * <li>Default is "-1", indicating to cache forever (just like
	 * <code>java.util.ResourceBundle</code>).
	 * <li>A positive number will cache loaded properties files for the given
	 * number of seconds. This is essentially the interval between refresh
	 * checks. Note that a refresh attempt will first check the last-modified
	 * timestamp of the file before actually reloading it; so if files don't
	 * change, this interval can be set rather low, as refresh attempts will not
	 * actually reload.
	 * <li>A value of "0" will check the last-modified timestamp of the file on
	 * every message access. <b>Do not use this in a production environment!</b>
	 * </ul>
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheMillis = (cacheSeconds * 1000);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder().append("\n");
		for (String msg : getProperties().keySet()) {
			str.append(msg);
			str.append(" = ");
			str.append(getProperties().get(msg));
			str.append("\n");
		}
		return str.toString();
	}
}
