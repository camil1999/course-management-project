package az.course.courseproject.author;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import az.course.courseproject.exception.MyValidationException;
import az.course.courseproject.request.CourseRequest;
import az.course.courseproject.response.CourseResponse;

@RestController
@RequestMapping(path = "/course")
@CrossOrigin(origins = "*")
public class CourseController {

	@Autowired
	private CourseService courseService;

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:courses')")
	public MappingJacksonValue findAll() {

		List<CourseResponse> dtos = courseService.findAll();
		return filter(dtos, "course", "id", "courseName", "status");
	}

	@PostMapping
	@PreAuthorize(value = "hasAuthority('created:course')")
	public void saveCourse(@Valid @RequestBody CourseRequest courseRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		courseService.saveCourse(courseRequest);
	}

	@PutMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('edit:course')")
	public void editCourse(@Valid @PathVariable Integer id, @RequestBody CourseRequest courseRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		courseService.editCourse(id, courseRequest);
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:course')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		CourseResponse courseResponse = courseService.findById(id);
		return filter(courseResponse, "course", "id", "courseName", "username");

	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:course')")
	public void deleteById(@PathVariable Integer id) {
		courseService.deleteById(id);

	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}

}
