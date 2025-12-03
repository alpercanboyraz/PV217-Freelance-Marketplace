package com.alpercan.freelance.frontend.client;

import com.alpercan.freelance.frontend.dto.MessageResponse;
import com.alpercan.freelance.frontend.dto.SendMessageRequest;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api/chat")
@RegisterRestClient(configKey = "chat-api")
public interface ChatServiceClient {

    @POST
    void sendMessage(@HeaderParam("Authorization") String token, SendMessageRequest request);

    @GET
    @Path("/{otherUserId}")
    List<MessageResponse> getConversation(@HeaderParam("Authorization") String token, @PathParam("otherUserId") Long otherUserId);

    @GET
    @Path("/conversations")
    List<Long> getConversationIds(@HeaderParam("Authorization") String token);
}