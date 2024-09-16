package dh.backend.clinica;
import dh.backend.clinica.dto.request.OdontologoRequestDto;
import dh.backend.clinica.dto.request.TurnoRequestDto;
import dh.backend.clinica.dto.response.OdontologoResponseDto;
import dh.backend.clinica.dto.response.TurnoResponseDto;
import dh.backend.clinica.entity.Domicilio;
import dh.backend.clinica.entity.Paciente;
import dh.backend.clinica.exception.ResourceNotFoundException;
import dh.backend.clinica.service.impl.OdontologoService;
import dh.backend.clinica.service.impl.PacienteService;
import dh.backend.clinica.service.impl.TurnoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class TurnoServiceTest {
    static final Logger logger = LoggerFactory.getLogger(TurnoServiceTest.class);
    @Autowired
    TurnoService turnoService;
    @Autowired
    OdontologoService odontologoService;
    @Autowired
    PacienteService pacienteService;
    TurnoRequestDto turno;
    TurnoResponseDto turnoDesdeDb;

    @BeforeEach
    void cargarDatos(){
        Domicilio domicilio = new Domicilio(null,"Falsa",145,"CABA","Buenos Aires");
        Paciente paciente = new Paciente();
        paciente.setApellido("Castro");
        paciente.setNombre("Maria");
        paciente.setDni("48974646");
        paciente.setFechaIngreso(LocalDate.of(2024,7,15));
        paciente.setDomicilio(domicilio);
        Paciente pacienteDesdeDb = pacienteService.guardarPaciente(paciente);
        OdontologoRequestDto odontologo = new OdontologoRequestDto("546563","Vilca","Julio");
        OdontologoResponseDto odontologoDesdeDb = odontologoService.guardarOdontologo(odontologo);
        turno = new TurnoRequestDto();
        turno.setFecha("2024-08-29");
        turno.setPaciente_id(pacienteDesdeDb.getId());
        turno.setOdontologo_id(odontologoDesdeDb.getId());
        turnoDesdeDb = turnoService.guardarTurno(turno);
    }

    @Test
    @DisplayName("Testear que un turno fue cargado correctamente")
    void caso1(){
        assertNotNull(turnoDesdeDb.getId());
    }

    @Test
    @DisplayName("Testear que un turno pueda acceder por id")
    void caso2(){
        //Dado
        Integer id = turnoDesdeDb.getId();
        //cuando
        TurnoResponseDto turnoRecuperado = turnoService.buscarPorId(id).get();
        // entonces
        assertEquals(id, turnoRecuperado.getId());
    }

    @Test
    @DisplayName("Listar todos los turnos")
    void caso3(){
        //Dado
        List<TurnoResponseDto> turnos;
        // cuando
        turnos = turnoService.buscarTodos();
        // entonces
        assertFalse(turnos.isEmpty());
    }

    @Test
    @DisplayName("Eliminar un turno")
    void caso4() {
        // Dado
        Integer id = turnoDesdeDb.getId();

        // Cuando se elimina
        turnoService.eliminarTurno(id);

        // Entonces
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            turnoService.buscarPorId(id).get();
        });

        assertTrue(exception.getMessage().contains("Turno no encontrado"));
    }
}