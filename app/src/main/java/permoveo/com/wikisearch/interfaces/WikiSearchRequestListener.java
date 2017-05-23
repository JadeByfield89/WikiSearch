package permoveo.com.wikisearch.interfaces;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import permoveo.com.wikisearch.model.SearchPage;

/**
 * Created by byfieldj on 5/22/17.
 */

public interface WikiSearchRequestListener {

    void onSearchRequestCompleted(ArrayList<SearchPage> results);

    void onSearchRequestError(VolleyError error);
}
