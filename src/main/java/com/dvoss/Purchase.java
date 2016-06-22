package com.dvoss;

import javax.persistence.*;

/**
 * Created by Dan on 6/22/16.
 */
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    String date;

    @Column(nullable = false)
    String card;

    @Column(nullable = false)
    int cvv;

    @Column(nullable = false)
    String category;

    @ManyToOne
    Customer customer;

    public Purchase() {
    }

    public Purchase(String date, String card, int cvv, String category, Customer customer) {
        this.date = date;
        this.card = card;
        this.cvv = cvv;
        this.category = category;
        this.customer = customer;
    }
}
