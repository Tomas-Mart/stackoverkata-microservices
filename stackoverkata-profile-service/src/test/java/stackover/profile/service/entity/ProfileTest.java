package stackover.profile.service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import stackover.profile.service.repository.ProfileRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
@Import(ValidationTestConfig.class)
public class ProfileTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        // Удаляем таблицу, если существует
        databaseClient.sql("DROP TABLE IF EXISTS profile").then().block();
        // Создаем таблицу заново
        databaseClient.sql(
                        "CREATE TABLE profile (" +
                                "id BIGSERIAL PRIMARY KEY, " +
                                "account_id BIGINT NOT NULL UNIQUE, " +
                                "email VARCHAR(255) NOT NULL UNIQUE, " +
                                "full_name VARCHAR(255) NOT NULL, " +
                                "city VARCHAR(255), " +
                                "persist_date TIMESTAMP, " +
                                "link_site VARCHAR(255), " +
                                "link_github VARCHAR(255), " +
                                "link_vk VARCHAR(255), " +
                                "about TEXT, " +
                                "image_link VARCHAR(255), " +
                                "last_redaction_date TIMESTAMP, " +
                                "nickname VARCHAR(255))")
                .then()
                .then(profileRepository.deleteAll())
                .block();
    }

    private Mono<Profile> validateAndSave(Profile profile) {
        // Валидация объекта
        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        if (!violations.isEmpty()) {
            return Mono.error(new ConstraintViolationException(violations));
        }

        // Установка временных меток
        LocalDateTime now = LocalDateTime.now();

        // Дата создания устанавливается только при первом сохранении
        if (profile.getPersistDateTime() == null) {
            profile.setPersistDateTime(now);
        }

        // Дата обновления всегда устанавливается при сохранении
        profile.setLastUpdateDateTime(now);

        return profileRepository.save(profile);
    }

    private Profile createInvalidProfileWithAccountId() {
        return Profile.builder()
                .accountId(0L) // Нарушает @Min(1)
                .email("test@mail.com")
                .fullName("test")
                .city("city")
                .linkSite("url.linkSite")
                .linkGitHub("url.linkGitHub")
                .linkVk("url.linkVk")
                .about("about")
                .imageLink("url.imageLink")
                .nickname("nickname")
                .build();
    }

    private Profile createInvalidProfileWithEmptyName() {
        return Profile.builder()
                .accountId(1L)
                .email("test@mail.com")
                .fullName("") // Нарушает @NotBlank
                .city("city")
                .linkSite("url.linkSite")
                .linkGitHub("url.linkGitHub")
                .linkVk("url.linkVk")
                .about("about")
                .imageLink("url.imageLink")
                .nickname("nickname")
                .build();
    }

    private Profile createValidProfile(Long accountId) {
        return Profile.builder()
                .accountId(accountId)
                .email("test" + accountId + "@mail.com") // Уникальный email
                .fullName("test")
                .city("city")
                .linkSite("url.linkSite")
                .linkGitHub("url.linkGitHub")
                .linkVk("url.linkVk")
                .about("about")
                .imageLink("url.imageLink")
                .nickname("nickname" + accountId)
                .build();
    }

    @Test
    void whenProfileIdIncorrect_thenThrowsException() {
        StepVerifier.create(validateAndSave(createInvalidProfileWithAccountId()))
                .expectError(ConstraintViolationException.class)
                .verify();
    }

    @Test
    void whenProfileNameIncorrect_thenThrowsException() {
        StepVerifier.create(validateAndSave(createInvalidProfileWithEmptyName()))
                .expectError(ConstraintViolationException.class)
                .verify();
    }

    @Test
    void whenProfileIsCorrect() {
        long accountId = System.currentTimeMillis();
        Profile validProfile = createValidProfile(accountId);

        // Verify the profile is valid before saving
        assertNotNull(validProfile, "Profile should be created");
        assertNotNull(validProfile.getEmail(), "Email should be set");

        // SAVE
        StepVerifier.create(validateAndSave(validProfile))
                .assertNext(savedProfile -> {
                    assertNotNull(savedProfile, "Saved profile should not be null");
                    assertNotNull(savedProfile.getId(), "ID should be generated after save");
                    assertNotNull(savedProfile.getPersistDateTime(), "Persist date should be set");
                    assertNotNull(savedProfile.getLastUpdateDateTime(), "Last update date should be set");

                    // Сравниваем с точностью до секунд
                    assertEquals(
                            savedProfile.getPersistDateTime().truncatedTo(ChronoUnit.SECONDS),
                            savedProfile.getLastUpdateDateTime().truncatedTo(ChronoUnit.SECONDS),
                            "Dates should be equal on creation"
                    );
                })
                .verifyComplete();

        // FIND
        StepVerifier.create(profileRepository.findByAccountId(accountId))
                .assertNext(foundProfile -> {
                    assertNotNull(foundProfile, "Profile should be found");
                    assertEquals(validProfile.getEmail(), foundProfile.getEmail());
                    assertEquals(
                            validProfile.getPersistDateTime().truncatedTo(ChronoUnit.SECONDS),
                            foundProfile.getPersistDateTime().truncatedTo(ChronoUnit.SECONDS)
                    );
                })
                .verifyComplete();

        // Даем небольшую задержку перед обновлением
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // UPDATE
        String newName = "newTest";
        Mono<Profile> updateOperation = profileRepository.findByAccountId(accountId)
                .flatMap(foundProfile -> {
                    foundProfile.setFullName(newName);
                    return validateAndSave(foundProfile);
                });

        StepVerifier.create(updateOperation)
                .assertNext(updatedProfile -> {
                    assertEquals(newName, updatedProfile.getFullName());
                    assertNotNull(updatedProfile.getLastUpdateDateTime());

                    // Проверяем что дата обновления не раньше даты создания
                    assertFalse(
                            updatedProfile.getLastUpdateDateTime().isBefore(updatedProfile.getPersistDateTime()),
                            "Update date should not be before creation date"
                    );
                })
                .verifyComplete();

        // VERIFY UPDATE
        StepVerifier.create(profileRepository.findByAccountId(accountId))
                .assertNext(updatedProfile -> {
                    assertEquals(newName, updatedProfile.getFullName());
                    assertNotNull(updatedProfile.getLastUpdateDateTime());
                })
                .verifyComplete();

        // DELETE
        Mono<Void> deleteOperation = profileRepository.findByAccountId(accountId)
                .flatMap(profile -> profileRepository.deleteById(profile.getId()));

        StepVerifier.create(deleteOperation)
                .verifyComplete();

        // VERIFY DELETE
        StepVerifier.create(profileRepository.findByAccountId(accountId))
                .verifyComplete(); // Expects no elements
    }

    @Test
    void whenDuplicateAccountId_thenThrowsException() {
        long accountId = System.currentTimeMillis();
        validateAndSave(createValidProfile(accountId)).block();

        StepVerifier.create(validateAndSave(createValidProfile(accountId)))
                .expectError(DataIntegrityViolationException.class)
                .verify();
    }
}