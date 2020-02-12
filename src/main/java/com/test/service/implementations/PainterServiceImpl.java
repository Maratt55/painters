package com.test.service.implementations;

import com.test.exceptions.DuplicateException;
import com.test.exceptions.NotFoundException;
import com.test.model.Painter;
import com.test.model.User;
import com.test.repository.PainterRepository;
import com.test.service.interfaces.PainterService;
import com.test.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Service
public class PainterServiceImpl implements PainterService {


    private final Logger logger = LoggerFactory.getLogger(BoughtPaintingsServiceImpl.class);

    @Autowired
    private PainterRepository painterRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @PersistenceContext
    private EntityManager entityManager;


    public void delete(int id) {
        painterRepository.deleteById(id);
    }

    @Transactional
    public void update(Painter painter) throws NotFoundException {
        Painter painter1 = painterRepository.getByEmail(painter.getEmail());
        if (painter1 == null) {
            throw new NotFoundException("User is not found");
        }
        painter1.setName(painter.getName());
        painter1.setPhone(painter.getPhone());
        painterRepository.save(painter1);
    }

    public Painter getByEmail(String email) throws NotFoundException {
        Painter painter = painterRepository.getByEmail(email);
        if (painter == null) {
            throw new NotFoundException("Painter is not found");
        }
        return painter;
    }

    public List<Painter> getAll() {
        return painterRepository.findAll();
    }

    public Painter getById(int id) {
        return painterRepository.getById(id);
    }

    @Transactional
    public void register(Painter painter) throws NotFoundException, DuplicateException {

        User user1 = null;
        String email = painter.getEmail();
        Painter painter1 = null;

        try {
            user1 = userService.getByEmail(email);
        } catch (NotFoundException e) {
            logger.info("User is not found");
        }
        User user = painter.getUser();
        if (user1 == null) {
            userService.register(user);
        } else {
            painter.setUser(user1);
        }
        try {
            painter1 = getByEmail(email);
        } catch (NotFoundException e) {
            logger.info("Painter is not found");
        }
        if (painter1 != null) {
            throw new DuplicateException("Duplicated painter data");
        }
        painter.setPassword(encoder.encode(painter.getPassword()));
        painterRepository.save(painter);
    }

    @Transactional
    public List<Painter> getByIdAndEmail(int id, String email) {
        String hql = "from Painter p where p.id = :id and p.email = :email";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", id);
        query.setParameter("email", email);
        List<Painter> painters = query.getResultList();
        return painters;
    }
}
