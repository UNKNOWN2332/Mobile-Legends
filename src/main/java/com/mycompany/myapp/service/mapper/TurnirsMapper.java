package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Turnirs;
import com.mycompany.myapp.service.dto.TurnirsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Turnirs} and its DTO {@link TurnirsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TurnirsMapper extends EntityMapper<TurnirsDTO, Turnirs> {}
