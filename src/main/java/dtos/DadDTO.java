package dtos;

public class DadDTO {
    public DadDTO(String url, String joke, String status) {
        this.url = url;
        this.joke = joke;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String url = "https://icanhazdadjoke.com/";
    private String joke;

    private String status;
}
