package com.test.controller;

import com.test.exceptions.NotFoundException;
import com.test.model.Painter;
import com.test.service.interfaces.PainterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/painter")
public class PainterController {

    @Autowired
    private PainterService painterService;

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable(value = "id") int id) {
        Painter painter = painterService.getById(id);
        return ResponseEntity.ok(painter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") int id) {
        painterService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity update(@Valid @RequestBody Painter painter) throws NotFoundException {
        painterService.update(painter);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email")
    public ResponseEntity getByEmail(@RequestParam(value = "email") String email) throws NotFoundException {
        Painter painter = painterService.getByEmail(email);
        return ResponseEntity.ok(painter);
    }

    @GetMapping
    public ResponseEntity getAll() {
        List<Painter> list = painterService.getAll();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/register")
    public ResponseEntity register(@Valid @RequestBody Painter painter) throws NotFoundException {
        painterService.register(painter);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id-and-email")
    public ResponseEntity getByIdAndEmail(@RequestParam(value = "id") int id,
                                          @RequestParam(value = "email") String email) {
        List<Painter> list = painterService.getByIdAndEmail(id, email);
        return ResponseEntity.ok(list);
    }
}
