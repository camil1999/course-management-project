package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import az.course.courseproject.model.Person;

@JsonFilter(value = "admin")
public class AdminResponse extends Person {

}
