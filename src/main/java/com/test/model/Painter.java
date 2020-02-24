package com.test.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;



@Entity
public class Painter extends AbstractModel {

    @OneToMany(mappedBy = "painter")
    @JsonManagedReference
    private List<Painting> paintings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk")
    @JsonBackReference
    private User user;

    public List<Painting> getPaintings() {
        return paintings;
    }

    public void setPaintings(List<Painting> paintings) {
        this.paintings = paintings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Painter{" +
                "paintings=" + paintings +
                ", user=" + user +
                '}';
    }
}
