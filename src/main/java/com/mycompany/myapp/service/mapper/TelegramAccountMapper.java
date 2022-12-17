package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.InfoPaid;
import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.service.dto.InfoPaidDTO;
import com.mycompany.myapp.service.dto.TelegramAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TelegramAccount} and its DTO {@link TelegramAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface TelegramAccountMapper extends EntityMapper<TelegramAccountDTO, TelegramAccount> {
    @Mapping(target = "infoPaid", source = "infoPaid", qualifiedByName = "infoPaidId")
    TelegramAccountDTO toDto(TelegramAccount s);

    @Named("infoPaidId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InfoPaidDTO toDtoInfoPaidId(InfoPaid infoPaid);
}
