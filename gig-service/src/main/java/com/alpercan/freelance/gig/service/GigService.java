package com.alpercan.freelance.gig.service;

import com.alpercan.freelance.gig.dto.GigRequest;
import com.alpercan.freelance.gig.model.Gig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class GigService {

    @Transactional
    public Gig createGig(GigRequest request, Long sellerId) {
        Gig gig = new Gig();
        gig.sellerId = sellerId; // Token'dan gelen ID
        gig.title = request.title();
        gig.description = request.description();
        gig.price = request.price();
        gig.category = request.category();
        gig.pictureUrl = request.pictureUrl();

        gig.persist();
        return gig;
    }
    public List<Gig> getGigsBySeller(Long sellerId) {
        return Gig.list("sellerId", sellerId); // Panache büyüsü! SQL: SELECT * FROM gigs WHERE sellerId = ?
    }
    public List<Gig> listAll() {
        return Gig.listAll();
    }

    public Gig getById(Long id) {
        return Gig.findById(id);
    }
}