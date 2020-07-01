package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
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

    public Uid requestToCardCompany(CardCompanyDto cardCompanyDto) {
        checkAndMakeUniqueUid(cardCompanyDto);
        return new Uid(
                cardCompanyRepository.saveAndFlush(cardCompanyDto.toEntity())
                        .getUid());
    }

    private void checkAndMakeUniqueUid(CardCompanyDto cardCompanyDto) {
        Uid candidateUid = cardCompanyDto.getUid();
        while (cardCompanyRepository.findByUid(candidateUid.getUid()).isPresent()) {
            candidateUid = cardCompanyDto.refreshUid();
        }
    }
}
