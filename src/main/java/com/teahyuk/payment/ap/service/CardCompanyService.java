package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.entity.StringData;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.repository.StringDataRepository;
import com.teahyuk.payment.ap.util.CryptoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardCompanyService {
    private final StringDataRepository stringDataRepository;

    @Autowired
    public CardCompanyService(StringDataRepository stringDataRepository) {
        this.stringDataRepository = stringDataRepository;
    }

    public Uid requestToCardCompany(CardCompanyDto cardCompanyDto) throws CryptoException {
        return new Uid(stringDataRepository.saveAndFlush(
                StringData.builder()
                        .string(cardCompanyDto.getSerializedString())
                        .uid(cardCompanyDto.getUid())
                        .build())
                .getUid());
    }
}
