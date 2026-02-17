package stackover.profile.service.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.profile.service.dto.AccountResponseDto;
import stackover.profile.service.dto.ProfilePostDto;
import stackover.profile.service.dto.ProfileRequestDto;
import stackover.profile.service.dto.ProfileResponseDto;
import stackover.profile.service.dto.converter.ProfileResponseDtoConverter;
import stackover.profile.service.entity.Profile;
import stackover.profile.service.feign.ReactiveAuthServiceClient;
import stackover.profile.service.repository.ProfileRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ReactiveAuthServiceClient reactiveAuthServiceClient;
    private final ProfileResponseDtoConverter profileResponseDtoConverter;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ReactiveAuthServiceClient reactiveAuthServiceClient,
                              ProfileResponseDtoConverter profileResponseDtoConverter) {
        this.profileRepository = profileRepository;
        this.reactiveAuthServiceClient = reactiveAuthServiceClient;
        this.profileResponseDtoConverter = profileResponseDtoConverter;
    }

    @Override
    public Mono<Void> saveProfileByPostDto(ProfilePostDto profilePostDto) {
        Profile profile = Profile.builder()
                .accountId(profilePostDto.accountId())
                .email(profilePostDto.email())
                .fullName(profilePostDto.fullName())
                .city(profilePostDto.city())
                .persistDateTime(profilePostDto.persistDateTime())
                .linkSite(profilePostDto.linkSite())
                .linkGitHub(profilePostDto.linkGitHub())
                .about(profilePostDto.about())
                .imageLink(profilePostDto.imageLink())
                .nickname(profilePostDto.nickname())
                .build();

        return profileRepository.save(profile).then();
    }

    @Override
    public Mono<Profile> findProfileByAccountId(Long accountId) {
        return profileRepository.findByAccountId(accountId);
    }

    @Override
    public Mono<ProfileResponseDto> getProfileResponseDtoById(Long accountId) {
        return profileRepository.findByAccountId(accountId)
                .flatMap(this::getProfileResponseDtoByProfile);
    }

    @Override
    public Mono<Boolean> existByAccountId(Long accountId) {
        return profileRepository.existsByAccountId(accountId);
    }

    @Override
    public Mono<ProfileResponseDto> updateProfileByIdAndProfileRequestDto(Long accountId, ProfileRequestDto requestDto) {
        return profileRepository.findByAccountId(accountId)
                .flatMap(profile -> {
                    profile.setEmail(requestDto.email());
                    profile.setFullName(requestDto.fullName());
                    profile.setCity(requestDto.city());
                    profile.setLinkSite(requestDto.linkSite());
                    profile.setLinkGitHub(requestDto.linkGitHub());
                    profile.setLinkVk(requestDto.linkVk());
                    profile.setAbout(requestDto.about());
                    profile.setImageLink(requestDto.imageLink());
                    profile.setNickname(requestDto.nickname());

                    return profileRepository.save(profile);
                })
                .flatMap(this::getProfileResponseDtoByProfile);
    }

    private Mono<ProfileResponseDto> getProfileResponseDtoByProfile(Profile profile) {
        ProfileResponseDto profileResponseDto = profileResponseDtoConverter.toDto(profile);

        return reactiveAuthServiceClient.getAccountById(profile.getAccountId())
                .map(response -> {
                    AccountResponseDto accountResponseDto = response.getBody();
                    profileResponseDto.setEmail(accountResponseDto.email());
                    profileResponseDto.setRole(accountResponseDto.role());
                    profileResponseDto.setEnabled(accountResponseDto.enabled());
                    return profileResponseDto;
                });
    }
}