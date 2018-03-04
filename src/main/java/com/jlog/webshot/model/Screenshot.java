package com.jlog.webshot.model;

import java.io.Serializable;

/**
 * @author Jose Luis Osorno <joseluis.osorno@ixxus.com>
 */
public class Screenshot implements Serializable {

    enum Engine {
        /** Headless Firefox */
        FF,
        /** Headless Chrome */
        CHR
    }

    private Engine engine;
    private String url;
    private Integer width;
    private Integer height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Screenshot that = (Screenshot)o;

        if (!url.equals(that.url))
            return false;
        if (width != null ? !width.equals(that.width) : that.width != null)
            return false;
        return height != null ? height.equals(that.height) : that.height == null;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }
}