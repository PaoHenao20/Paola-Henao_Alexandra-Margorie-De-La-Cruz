package dh.backend.clinica.controller;

import dh.backend.clinica.dto.request.OdontologoRequestDto;
import dh.backend.clinica.dto.response.OdontologoResponseDto;
import dh.backend.clinica.entity.Odontologo;
import dh.backend.clinica.service.IOdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/odontologo")
public class OdontologoController {
    private IOdontologoService odontologoService;

    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<OdontologoResponseDto> agregarOdontologo(@RequestBody OdontologoRequestDto odontologoRequestDto){
        return ResponseEntity.ok(odontologoService.guardarOdontologo(odontologoRequestDto));
    }

    //PUT
    @PutMapping("/modificar")
    public ResponseEntity<String>  modificarOdontologo(@RequestBody OdontologoResponseDto odontologoResponseDto){
        odontologoService.modificarOdontologo(odontologoResponseDto);
        return ResponseEntity.ok("{\"mensaje\": \"El odontologo fue modificado\"}");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Integer id){
        odontologoService.eliminarOdontologo(id);
        return ResponseEntity.ok("{\"mensaje\": \"El odontologo fue eliminado\"}");
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<OdontologoResponseDto> buscarPorId(@PathVariable Integer id){
        Optional<OdontologoResponseDto> odontologo = odontologoService.buscarPorId(id);
        if(odontologo.isPresent()){
            return ResponseEntity.ok(odontologo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/buscartodos")
    public ResponseEntity<List<OdontologoResponseDto>>  buscarTodos(){
        return ResponseEntity.ok(odontologoService.buscarTodos());
    }


    @GetMapping("/buscarApellido/{parte}")
    public ResponseEntity<List<OdontologoResponseDto>> buscarParteApellido(@PathVariable String parte){
        return ResponseEntity.ok(odontologoService.buscarPorUnaParteApellido(parte));
    }

}
