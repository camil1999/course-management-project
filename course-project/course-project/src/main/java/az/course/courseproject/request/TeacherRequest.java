package az.course.courseproject.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TeacherRequest {
	private Integer id;
	@NotEmpty(message = "Ad hissəsini boş qoymaq olmaz!")
	@Size(min = 3, message = "Ad minimum 3 simvol olmalıdır!")
	@Size(max = 10, message = "Ad maksimum 10 simvol olmalıdır!")
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
