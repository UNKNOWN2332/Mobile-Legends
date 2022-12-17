package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Clans;
import com.mycompany.myapp.service.dto.ClansDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Clans} and its DTO {@link ClansDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClansMapper extends EntityMapper<ClansDTO, Clans> {}
