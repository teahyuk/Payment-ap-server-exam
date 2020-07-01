package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CardCompanyService {
    private final CardCompanyRepository cardCompanyRepository;

    @Autowired
    public CardCompanyService(CardCompanyRepository cardCompanyRepository) {
        this.cardCompanyRepository = cardCompanyRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Uid requestToCardCompany(RequestToCompanyObject requestToCompanyObject) {
        Uid uid = checkAndMakeUniqueUid(requestToCompanyObject);
        cardCompanyRepository.saveAndFlush(requestToCompanyObject.toEntity(uid));
        return uid;
    }

    private Uid checkAndMakeUniqueUid(RequestToCompanyObject requestToCompanyObject) {
        Uid candidateUid = requestToCompanyObject.createUid();
        while (cardCompanyRepository.findByUid(candidateUid.getUid()).isPresent()) {
            candidateUid = requestToCompanyObject.createUid();
        }
        return candidateUid;
    }

    @Transactional
    public Optional<Uid> requestToCardCompany(Cancel cancel) {
        return cardCompanyRepository.findByUid(cancel.getOriginUid().getUid())
                .map(cardCompany ->
                        requestToCardCompany(cancel.getRequestToObject(cardCompany.getCardInfo())));
    }
}
