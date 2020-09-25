package com.company;

import java.util.Objects;

public class Data {
    private String url;
    private String name;

    public Data(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Название: " + "\"" + name + "\"" + "\n" +
                "Ссылка: '" + url + "'" + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Data other = (Data) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }

        if (!Objects.equals(this.url, other.url)) {
            return false;
        }

        return true;
    }
}
