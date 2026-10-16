package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Equipo;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.service.dto.EquipoDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VentanaDeMantenimiento} and its DTO {@link VentanaDeMantenimientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentanaDeMantenimientoMapper extends EntityMapper<VentanaDeMantenimientoDTO, VentanaDeMantenimiento> {
    @Mapping(target = "equipo", source = "equipo", qualifiedByName = "equipoNombre")
    @Mapping(target = "creadaPor", source = "creadaPor", qualifiedByName = "userLogin")
    VentanaDeMantenimientoDTO toDto(VentanaDeMantenimiento s);

    @Named("equipoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EquipoDTO toDtoEquipoNombre(Equipo equipo);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
