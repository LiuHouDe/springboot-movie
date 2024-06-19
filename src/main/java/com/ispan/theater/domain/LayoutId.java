package com.ispan.theater.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class LayoutId implements Serializable {
    private static final long serialVersionUID = -4738485060507780668L;
    @Column(name = "auditorium_id", nullable = false)
    private Integer auditoriumId;

    @Column(name = "seat_id", nullable = false)
    private Integer seatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LayoutId entity = (LayoutId) o;
        return Objects.equals(this.auditoriumId, entity.auditoriumId) &&
                Objects.equals(this.seatId, entity.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auditoriumId, seatId);
    }

}