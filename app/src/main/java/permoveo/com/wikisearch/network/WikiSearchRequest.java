package permoveo.com.wikisearch.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import permoveo.com.wikisearch.application.WikiSearchApplication;
import permoveo.com.wikisearch.constant.AppConstants;
import permoveo.com.wikisearch.interfaces.WikiSearchRequestListener;
import permoveo.com.wikisearch.model.SearchImage;
import permoveo.com.wikisearch.model.SearchPage;

/**
 * Created by byfieldj on 5/22/17.
 */

public class WikiSearchRequest {

    private WikiSearchRequestListener mListener;
    private static final String TAG = "WikiSearchRequest";
    private static final String KEY_QUERY = "query";
    private static final String KEY_PAGES = "pages";
    private static final String KEY_ORIGINAL = "original";

    private ArrayList<SearchPage> mPages = new ArrayList<>();


    public WikiSearchRequest() {
    }

    public void performSearch(String query, WikiSearchRequestListener listener) {
        this.mListener = listener;

        if(query.contains(" ")){
            query = query.replace(" ", "%20");
            Log.d(TAG, "Found space in query, removing!");
        }

        String search_url = String.format(AppConstants.WIKI_SEARCH_URL, query);
        Log.d(TAG, "Searching at " + search_url);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, search_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject queryObject = response.getJSONObject(KEY_QUERY);
                    Log.d(TAG, "Query Object -> " + queryObject.toString());

                    JSONObject pagesObject = queryObject.getJSONObject(KEY_PAGES);
                    Log.d(TAG, "Pages Object -> " + pagesObject.toString());

                    for (Iterator<String> iter = pagesObject.keys(); iter.hasNext(); ) {
                        String key = iter.next();
                        Log.d(TAG, "Key -> " + key);

                        if (key != null && !key.isEmpty()) {
                            JSONObject page = (JSONObject) pagesObject.get(key);
                            Log.d(TAG, "Page object -> " + page.toString());

                            SearchPage searchPage = new SearchPage(page);

                            if (page.has(KEY_ORIGINAL)) {
                                JSONObject imageObject = (JSONObject) page.get(KEY_ORIGINAL);
                                Log.d(TAG, "Image Object -> " + imageObject);

                                SearchImage image = new SearchImage(imageObject);
                                searchPage.setImage(image);
                                mPages.add(searchPage);

                            }
                        }

                    }

                    mListener.onSearchRequestCompleted(mPages);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mListener.onSearchRequestError(error);
            }
        });

        WikiSearchApplication.getInstance().addToRequestQueue(request);
    }
}
