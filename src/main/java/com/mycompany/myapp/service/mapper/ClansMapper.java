package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Clans;
import com.mycompany.myapp.domain.Turnirs;
import com.mycompany.myapp.service.dto.ClansDTO;
import com.mycompany.myapp.service.dto.TurnirsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Clans} and its DTO {@link ClansDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClansMapper extends EntityMapper<ClansDTO, Clans> {
    @Mapping(target = "turnirs", source = "turnirs", qualifiedByName = "turnirsId")
    ClansDTO toDto(Clans s);

    @Named("turnirsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TurnirsDTO toDtoTurnirsId(Turnirs turnirs);
}
