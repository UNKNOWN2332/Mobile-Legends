package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.InfoPaid;
import com.mycompany.myapp.service.dto.InfoPaidDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InfoPaid} and its DTO {@link InfoPaidDTO}.
 */
@Mapper(componentModel = "spring")
public interface InfoPaidMapper extends EntityMapper<InfoPaidDTO, InfoPaid> {}
