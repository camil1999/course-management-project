package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import az.course.courseproject.model.Person;

@JsonFilter(value = "student")
public class StudentResponse extends Person {

}
