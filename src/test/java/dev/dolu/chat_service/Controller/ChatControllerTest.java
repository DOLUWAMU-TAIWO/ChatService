package dev.dolu.chat_service.Controller;

import dev.dolu.chat_service.Repo.ChatRepository;
import dev.dolu.chat_service.model.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRepository chatRepository;

    private Chat chatRequest;

    @BeforeEach
    void setUp() {
        chatRequest = new Chat();
        chatRequest.setParticipants(Collections.singletonList("user1"));
    }

//    @Test
//    void startChat_ShouldReturnBadRequest_WhenParticipantsAreLessThanTwo() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/chats")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"participants\": [\"user1\"]}")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void startChat_ShouldReturnCreated_WhenValidRequest() throws Exception {
//        chatRequest.setParticipants(Arrays.asList("user1", "user2"));
//        Mockito.when(chatRepository.save(Mockito.any(Chat.class))).thenReturn(chatRequest);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/chats")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"participants\": [\"user1\", \"user2\"]}")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.participants").isArray())
//                .andExpect(jsonPath("$.participants[0]").value("user1"))
//                .andExpect(jsonPath("$.participants[1]").value("user2"));
//    }
}