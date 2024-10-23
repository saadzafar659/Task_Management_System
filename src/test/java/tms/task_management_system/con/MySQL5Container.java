package tms.task_management_system.con;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQL5Container extends MySQLContainer<MySQL5Container> {
	private static final String IMAGE_VERSION = "mysql:5.7";
	private static MySQL5Container container;

	private MySQL5Container() {
		super(DockerImageName.parse(IMAGE_VERSION));
		withDatabaseName("mydb");
		withUsername("root");
		withPassword("root");
	}

	public static MySQL5Container getInstance() {
		if (container == null) {
			container = new MySQL5Container();
		}
		return container;
	}

	@Override
	public void start() {
		super.start();
		System.setProperty("DB_URL", container.getJdbcUrl());
		System.setProperty("DB_USERNAME", container.getUsername());
		System.setProperty("DB_PASSWORD", container.getPassword());
	}

}
