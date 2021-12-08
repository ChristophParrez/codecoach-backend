package be.codecoach.api.dtos;

import be.codecoach.domain.RoleEnum;

public class RoleDto {

    private final String roleId;
    private final RoleEnum role;

    private RoleDto(String roleId, RoleEnum role) {
        this.roleId = roleId;
        this.role = role;
    }

    public String getRoleId() {
        return roleId;
    }

    public RoleEnum getRole() {
        return role;
    }


    public static final class Builder {
        private String roleId;
        private RoleEnum role;

        private Builder() {
        }

        public static Builder aRoleDto() {
            return new Builder();
        }

        public Builder withRoleId(String roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder withRole(RoleEnum role) {
            this.role = role;
            return this;
        }

        public RoleDto build() {
            return new RoleDto(roleId, role);
        }
    }
}
