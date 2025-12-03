package com.alpercan.freelance.gig.service;

import com.alpercan.freelance.gig.dto.GigRequest;
import com.alpercan.freelance.gig.model.Gig;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@ApplicationScoped
public class GigService {

    @Transactional
    public Gig createGig(GigRequest request, Long sellerId) {
        Gig gig = new Gig();
        gig.sellerId = sellerId;
        gig.title = request.title();
        gig.description = request.description();
        gig.price = request.price();
        gig.category = request.category();
        gig.pictureUrl = request.pictureUrl();

        gig.persist();
        return gig;
    }
    public List<Gig> getGigsBySeller(Long sellerId) {
        return Gig.list("sellerId", sellerId);
    }
    public List<Gig> listAll() {
        return Gig.listAll();
    }

    public Gig getById(Long id) {
        return Gig.findById(id);
    }
    public List<Gig> getMyGigs(Long sellerId) { return Gig.list("sellerId", sellerId); }

    @Transactional
    public boolean deleteGig(Long gigId, Long userId) {

        Gig gig = Gig.findById(gigId);


        if (gig != null && gig.sellerId.equals(userId)) {
            gig.delete();
            return true;
        }
        return false;
    }
    public List<Gig> search(String category, String sort) {

        Sort sortObj = Sort.descending("createdAt");

        if ("price_asc".equals(sort)) {
            sortObj = Sort.ascending("price");
        } else if ("price_desc".equals(sort)) {
            sortObj = Sort.descending("price");
        }


        if (category != null && !category.isEmpty()) {
            return Gig.list("category", sortObj, category);
        } else {
            return Gig.listAll(sortObj);
        }
    }

}