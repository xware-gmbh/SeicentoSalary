package ch.xwr.seicentobookit.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TheVersion {
	/** Logger initialized */
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TheVersion.class);
	
	private final Properties prop;

	public TheVersion() {
		final InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");
		this.prop = new Properties();
		try {
			this.prop.load(resourceAsStream);
		} catch (final IOException e) {
			LOG.error(e);
		}

//		System.out.println("Version: " + getEntryById("version"));
//		System.out.println("groupId: " + getEntryById("groupId"));
//		System.out.println("artifactId: " + getEntryById("artifactId"));
	}

	public String getEntryById(final String id) {
		return this.prop.getProperty(id);
	}

}
