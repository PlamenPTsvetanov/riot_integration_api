package me.riot.integration.api.icon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import me.riot.integration.api._common.datamodel.BaseOrmBean;

@Getter
@Setter
@Entity
@Table(name = "icon", schema = "riot_integration")
public class IconOrmBean extends BaseOrmBean {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotNull
    @Column(name = "riot_id", nullable = false)
    private Integer riotId;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "image", nullable = false)
    private byte[] image;

}