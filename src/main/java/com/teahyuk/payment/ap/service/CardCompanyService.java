package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.entity.CardCompany;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import com.teahyuk.payment.ap.util.CryptoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardCompanyService {
    private final CardCompanyRepository cardCompanyRepository;

    @Autowired
    public CardCompanyService(CardCompanyRepository cardCompanyRepository) {
        this.cardCompanyRepository = cardCompanyRepository;
    }

    public Uid requestToCardCompany(CardCompanyDto cardCompanyDto) throws CryptoException {
        return new Uid(cardCompanyRepository.saveAndFlush(
                CardCompany.builder()
                        .string(cardCompanyDto.getSerializedString())
                        .uid(cardCompanyDto.getUid())
                        .build())
                .getUid());
    }
}
