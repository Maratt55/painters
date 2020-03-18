package com.test.service.implementations;

import com.test.model.xml.Currency;
import com.test.model.xml.CurrencyArray;
import com.test.repository.CurrencyRepository;
import com.test.service.interfaces.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Autowired
    private CurrencyRepository currencyRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public Currency getById(int id) {
        return currencyRepository.getById(id);
    }

    @Transactional
    public void save() {
        try {
            JAXBContext context = JAXBContext.newInstance(CurrencyArray.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            File file = new File(getClass().getClassLoader().getResource("currency.xml").getFile());
            CurrencyArray currencyArray = (CurrencyArray) jaxbUnmarshaller.unmarshal(file);
            List<Currency> list = currencyArray.getCurrencies();
            currencyRepository.saveAll(list);
        } catch (JAXBException e) {
            logger.info("Could not unmarshalling");
        }
    }

    @Transactional
    public List<Currency> getAll(Pageable pageable) {
        String[] sort = pageable.getSort().toString().split(":");
        String hql = "from Currency order by ";
        String s = Arrays.stream(sort).reduce(String.valueOf(" "), (s1, s2) -> s1 + s2);
        Query query = entityManager.createQuery(hql + s);
        int size = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);
        List<Currency> currencies = query.getResultList();
        return currencies;
    }
}

