package dtos;

import entities.User;

import java.util.List;
import java.util.Objects;

public class UserDTO {
    public UserDTO(long id, String userName, String userPass, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.userPass = userPass;
        this.roles = roles;
    }

    private long id;
    private String userName;
    private String userPass;
    private List<String> roles;

    public UserDTO(User u){
        this.id = u.getId();
        this.userName = u.getUserName();
        this.userPass = u.getUserPass();
        this.roles = u.getRolesAsStrings();
    }

    public UserDTO(String userName, String userPass, List<String> roles) {
        this.userName = userName;
        this.userPass = userPass;
        this.roles = roles;
    }

    public UserDTO(Long id, String userName, String userPass, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.userPass = userPass;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return getId() == userDTO.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", roles=" + roles +
                '}';
    }
}
