package entity;

public class UserRoles {
    int roleId;
    String name;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRoles(int roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }
}
