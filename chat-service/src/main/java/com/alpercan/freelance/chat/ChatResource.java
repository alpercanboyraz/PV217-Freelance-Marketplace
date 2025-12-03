package com.alpercan.freelance.chat;

import com.alpercan.freelance.chat.dto.SendMessageRequest;
import com.alpercan.freelance.chat.model.Message;
import com.alpercan.freelance.chat.service.ChatService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

@Path("/api/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated // Sadece giriş yapmış kullanıcılar!
public class ChatResource {

    @Inject
    ChatService chatService;

    @Inject
    JsonWebToken jwt;

    @POST
    public Response sendMessage(SendMessageRequest request) {
        // Gönderen benim (Token'dan ID'yi al)
        Long senderId = Long.parseLong(jwt.getClaim("userId").toString());

        Message message = chatService.sendMessage(senderId, request);
        return Response.status(Response.Status.CREATED).entity(message).build();
    }

    @GET
    @Path("/{otherUserId}")
    public List<Message> getConversation(@PathParam("otherUserId") Long otherUserId) {
        // Ben (Token) ile Diğer Kişi (URL) arasındaki mesajlar
        Long myId = Long.parseLong(jwt.getClaim("userId").toString());

        return chatService.getConversation(myId, otherUserId);
    }
    @GET
    @Path("/conversations")
    public List<Long> getConversations() {
        Long myId = Long.parseLong(jwt.getClaim("userId").toString());
        List<Message> sent = Message.list("senderId", myId);

        List<Message> received = Message.list("receiverId", myId);

        return Stream.concat(
                sent.stream().map(m -> m.receiverId),
                received.stream().map(m -> m.senderId)
        ).distinct().collect(Collectors.toList());
    }
}