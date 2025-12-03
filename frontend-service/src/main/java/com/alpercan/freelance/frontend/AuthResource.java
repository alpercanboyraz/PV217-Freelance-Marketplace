package com.alpercan.freelance.frontend;

import com.alpercan.freelance.frontend.service.AuthService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/auth")
public class AuthResource {

    @Inject
    Template login;

    @Inject
    Template register;

    @Inject
    AuthService authService; // Artık Client değil Service kullanıyoruz

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getLoginPage() {
        return login.instance();
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getRegisterPage() {
        return register.instance();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response performRegister(@FormParam("username") String username,
                                    @FormParam("password") String password,
                                    @FormParam("email") String email,
                                    @FormParam("fullName") String fullName,
                                    @FormParam("role") String role) {
        try {
            // İş mantığı Service'e devredildi
            authService.register(username, password, email, fullName, role);

            // Resource sadece yönlendirme yapıyor
            return Response.seeOther(URI.create("/auth/login")).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/auth/register?error=true")).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response performLogin(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            // Token'ı servisten al
            String token = authService.login(email, password);

            // Cookie oluşturmak HTTP işidir, o yüzden Resource katmanında kalabilir
            NewCookie cookie = new NewCookie.Builder("jwt_token")
                    .value(token)
                    .path("/")
                    .httpOnly(true)
                    .build();

            return Response.seeOther(URI.create("/"))
                    .cookie(cookie)
                    .build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/auth/login?error=true")).build();
        }
    }

    // Çıkış Yapma (Logout) - Cookie'yi siler
    @GET
    @Path("/logout")
    public Response logout() {
        NewCookie deleteCookie = new NewCookie.Builder("jwt_token")
                .value("")
                .path("/")
                .maxAge(0) // Anında sil
                .build();

        return Response.seeOther(URI.create("/")).cookie(deleteCookie).build();
    }
}