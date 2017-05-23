package permoveo.com.wikisearch.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by byfieldj on 5/22/17.
 *
 * This model class represents a single Image result, taken from each of our page results. Holds the source url and height/width parameters
 * in case we need them later.
 */

public class SearchImage implements Serializable {


    private String mSource;
    private int mHeight;
    private int mWidth;

    private static final String KEY_SOURCE = "source";
    public static final String KEY_IMAGE_HEIGHT = "height";
    public static final String KEY_IMAGE_WIDTH = "width";


    public SearchImage(JSONObject object) {

        if (object != null) {
            try {
                if (object.has(KEY_SOURCE)) {
                    setSource(object.getString(KEY_SOURCE));
                }

                if (object.has(KEY_IMAGE_HEIGHT)) {
                    setHeight(object.getInt(KEY_IMAGE_HEIGHT));
                }

                if (object.has(KEY_IMAGE_WIDTH)) {
                    setWidth(object.getInt(KEY_IMAGE_WIDTH));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSource(final String source) {
        this.mSource = source;
    }

    public String getSource() {
        return mSource;
    }

    public void setHeight(final int height) {
        this.mHeight = height;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setWidth(final int width) {
        this.mWidth = width;
    }

    public int getWidth() {
        return mWidth;
    }
}
