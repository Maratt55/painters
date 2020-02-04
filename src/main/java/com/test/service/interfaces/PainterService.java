package com.test.service.interfaces;

import com.test.exceptions.NotFoundException;
import com.test.model.AbstractModel;
import com.test.model.Painter;

import java.util.List;


public interface PainterService {

    Painter getById(int id);

    void delete(int id);

    void update(Painter painter) throws NotFoundException;

    Painter getByEmail(String email) throws NotFoundException;

    List<Painter> getAll();

    void register(Painter painter) throws NotFoundException;

    List<Painter> getByIdAndEmail(int id, String email);
}
