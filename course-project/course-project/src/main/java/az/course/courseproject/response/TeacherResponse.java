package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import az.course.courseproject.model.Person;

@JsonFilter(value = "teacher")
public class TeacherResponse extends Person {

}
