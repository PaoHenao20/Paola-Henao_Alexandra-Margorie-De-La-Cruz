package dh.backend.clinica.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OdontologoRequestDto {
    private String matricula;
    private String apellido;
    private String nombre;
}
