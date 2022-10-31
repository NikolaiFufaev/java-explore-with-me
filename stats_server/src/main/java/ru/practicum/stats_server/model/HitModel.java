package ru.practicum.stats_server.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class HitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 512)
    @Column(name = "app")
    private String app;

    @Size(max = 512)
    @Column(name = "uri")
    private String uri;

    @Size(max = 30)
    @Column(name = "ip")
    private String ip;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HitModel hitModel = (HitModel) o;
        return id != null && Objects.equals(id, hitModel.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
