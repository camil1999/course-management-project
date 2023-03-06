package az.course.courseproject.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
@Data
public class Person {
	private String name;
	@NotEmpty(message = "Soyad hissəsini boş qoymaq olmaz!")
	@Size(min = 3, message = "Soyad minimum 3 simvol olmalıdır!")
	@Size(max = 10, message = "Soyad maksimum 10 simvol olmalıdır!")
	private String surname;
	@NotEmpty(message = "İstifadəçi adını boş qoymaq olmaz!")
	@Size(min = 3, message = "İstifadəçi adı minimum 3 simvol olmalıdır!")
	@Size(max = 10, message = "İstifadəçi adı maksimum 10 simvol olmalıdır!")
	private String username;
	@NotEmpty(message = "Şifrəni boş qoymaq olmaz!")
	@Size(min = 3, message = "Şifrə minimum 3 simvol olmalıdır!")
	private String password;
}
