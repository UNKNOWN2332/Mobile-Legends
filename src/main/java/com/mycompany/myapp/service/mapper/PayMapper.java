package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Pay;
import com.mycompany.myapp.domain.Period;
import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.service.dto.PayDTO;
import com.mycompany.myapp.service.dto.PeriodDTO;
import com.mycompany.myapp.service.dto.TelegramAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pay} and its DTO {@link PayDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayMapper extends EntityMapper<PayDTO, Pay> {
    @Mapping(target = "account", source = "account", qualifiedByName = "telegramAccountId")
    @Mapping(target = "period", source = "period", qualifiedByName = "periodId")
    PayDTO toDto(Pay s);

    @Named("telegramAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TelegramAccountDTO toDtoTelegramAccountId(TelegramAccount telegramAccount);

    @Named("periodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PeriodDTO toDtoPeriodId(Period period);
}
