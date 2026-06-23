package Connection.Service.connection_servicee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ConnectionServiceeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectionServiceeApplication.class, args);
	}

}
