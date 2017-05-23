package permoveo.com.wikisearch.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import permoveo.com.wikisearch.constant.AppConstants;

/**
 * Created by byfieldj on 5/22/17.
 *
 * This model class represents a single page result based on a given query
 */

public class SearchPage implements Serializable {

    private String mTitle;
    private int mPageId;
    private SearchImage mImage;

    public SearchPage(JSONObject object) {

        if (object != null) {

            try {
                if (object.has(AppConstants.PAGE_TITLE)) {
                    setTitle(object.getString(AppConstants.PAGE_TITLE));
                }

                if (object.has(AppConstants.PAGE_ID)) {
                    setPageId(object.getInt(AppConstants.PAGE_ID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void setTitle(final String title) {
        this.mTitle = title;
    }

    public String getTitle() {

        return mTitle;
    }

    public void setPageId(final int pageId) {
        this.mPageId = pageId;
    }

    public int getPageId() {

        return mPageId;
    }


    public void setImage(final SearchImage image) {
        this.mImage = image;
    }

    public SearchImage getImage() {
        return mImage;
    }


}
