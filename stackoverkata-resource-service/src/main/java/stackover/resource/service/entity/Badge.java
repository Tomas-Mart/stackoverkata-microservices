package stackover.resource.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @Column(name = "badge_name")
    private String badgeName;

    @Column(name = "reputations_for_merit")
    private Integer reputationForMerit;

    @Column
    private String description;
}
