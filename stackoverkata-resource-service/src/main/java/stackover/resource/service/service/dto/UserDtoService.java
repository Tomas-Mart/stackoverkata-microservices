package stackover.resource.service.service.dto;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.UserResponseDto;

@Service
public interface UserDtoService {

    Mono<UserResponseDto> getUserDtoByUserId(Long userId);
} 