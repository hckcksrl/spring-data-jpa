package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @EnableJpaRepositories basePackages 를 해줘서 위치를 잡아줘야 하지만
 * SpringBoot을 사용하면 없어도 된다.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "study.datajpa.repository")
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

}
