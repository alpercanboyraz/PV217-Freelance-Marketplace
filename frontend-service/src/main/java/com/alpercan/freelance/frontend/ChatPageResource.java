package com.alpercan.freelance.frontend;

import com.alpercan.freelance.frontend.dto.ConversationDTO; // EKLENDİ
import com.alpercan.freelance.frontend.dto.MessageResponse;
import com.alpercan.freelance.frontend.service.FrontendChatService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.util.List;

@Path("/chat")
public class ChatPageResource {

    @Inject
    @Location("chat.html")
    Template chatTemplate;

    @Inject
    @Location("inbox.html")
    Template inboxTemplate; // EKLENDİ

    @Inject
    FrontendChatService chatService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/start/{userId}")
    @Produces(MediaType.TEXT_HTML)
    public Response openChat(@CookieParam("jwt_token") String token, @PathParam("userId") Long otherUserId) {
        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        try {
            List<MessageResponse> messages = chatService.getConversation(token, otherUserId);

            // Basit ID alma yöntemi (JWT library parsing yerine)
            Long myId = getMyUserIdFromToken(token);

            return Response.ok(
                    chatTemplate.data("messages", messages)
                            .data("otherUserId", otherUserId)
                            .data("myId", myId)
            ).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.seeOther(URI.create("/?error=true")).build();
        }
    }

    @POST
    @Path("/send/{userId}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendMessage(@CookieParam("jwt_token") String token,
                                @PathParam("userId") Long receiverId,
                                @FormParam("content") String content) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        try {
            chatService.sendMessage(token, receiverId, content);
            return Response.seeOther(URI.create("/chat/start/" + receiverId)).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/?error=true")).build();
        }
    }

    // --- INBOX SAYFASI ---
    @GET
    @Path("/inbox")
    @Produces(MediaType.TEXT_HTML)
    public Response showInbox(@CookieParam("jwt_token") String token) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        List<ConversationDTO> conversations = chatService.getInbox(token);
        return Response.ok(inboxTemplate.data("conversations", conversations)).build();
    }

    private Long getMyUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            int index = payload.indexOf("\"userId\":");
            if (index != -1) {
                String sub = payload.substring(index + 9);
                String idStr = sub.split("[,}]")[0];
                return Long.parseLong(idStr.trim());
            }
        } catch (Exception e) {
            // ignore
        }
        return 0L;
    }
}