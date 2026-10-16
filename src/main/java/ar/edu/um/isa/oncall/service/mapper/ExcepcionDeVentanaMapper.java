package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.service.dto.ExcepcionDeVentanaDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExcepcionDeVentana} and its DTO {@link ExcepcionDeVentanaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExcepcionDeVentanaMapper extends EntityMapper<ExcepcionDeVentanaDTO, ExcepcionDeVentana> {
    @Mapping(target = "ventana", source = "ventana", qualifiedByName = "ventanaDeMantenimientoNombre")
    @Mapping(target = "autorizadaPor", source = "autorizadaPor", qualifiedByName = "userLogin")
    ExcepcionDeVentanaDTO toDto(ExcepcionDeVentana s);

    @Named("ventanaDeMantenimientoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    VentanaDeMantenimientoDTO toDtoVentanaDeMantenimientoNombre(VentanaDeMantenimiento ventanaDeMantenimiento);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
