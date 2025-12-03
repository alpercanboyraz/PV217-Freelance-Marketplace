package com.alpercan.freelance.frontend;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.File;

@Path("/uploads")
public class ImageResource {

    private static final String UPLOAD_DIR = "uploads/";

    @GET
    @Path("/{fileName}")
    @Produces("image/jpeg") // Veya image/png, tarayıcı genelde anlar
    public Response getImage(@PathParam("fileName") String fileName) {
        File file = new File(UPLOAD_DIR + fileName);

        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Dosyayı al ve cevap olarak gönder
        return Response.ok(file).build();
    }
}