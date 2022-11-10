package dtos;

public class ChuckDTO {
    public ChuckDTO(String id, String url, String value) {
        this.id = id;
        this.url = url;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String id;
    private String url = "https://api.chucknorris.io/jokes/random";
    private String value;


}
