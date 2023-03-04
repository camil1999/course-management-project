package az.course.courseproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseProjectApplication.class, args);
	}
	
	@Bean
	public ModelMapper mapper() {
		ModelMapper m=new ModelMapper();
		return m;
	}

}
