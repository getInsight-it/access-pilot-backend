package it.getinsight.module.level.dto;

import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleResponseResumedDTO;
import lombok.Data;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public final class ItemResponseNodeDTO {
    private final Long id;
    private final String name;
    private final LevelResumedDTO level;
    private final RoleResponseResumedDTO role;
    private final List<ItemResponseNodeDTO> items = new ArrayList<>();

    public ItemResponseNodeDTO(Long id, String name, LevelResumedDTO level, RoleDTO role) {
        this.id = id;
        this.name = name != null ? WordUtils.capitalizeFully(name) : null;
        this.level = level;
        this.role = RoleResponseResumedDTO.builder()
            .id(role.id())
            .name(role.name())
            .description(role.description())
            .build();
    }


}
