package dev.dolu.chat_service.Config;


import dev.dolu.chat_service.Service.LocalUserServiceClient;
import dev.dolu.chat_service.Service.RestUserServiceClient;
import dev.dolu.chat_service.Service.UserServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserServiceClientConfig {

    @Bean
    public UserServiceClient userServiceClient(
            LocalUserServiceClient localUserServiceClient,
            RestUserServiceClient restUserServiceClient,
            @org.springframework.beans.factory.annotation.Value("${chat.useLocalUserService:true}") boolean useLocalUserService
    ) {
        return useLocalUserService ? localUserServiceClient : restUserServiceClient;
    }
}
