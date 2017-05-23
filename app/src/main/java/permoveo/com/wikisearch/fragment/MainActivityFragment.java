package permoveo.com.wikisearch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.wikisearch.R;
import permoveo.com.wikisearch.activity.ImageInfoActivity;
import permoveo.com.wikisearch.adapter.ImageSearchAdapter;
import permoveo.com.wikisearch.interfaces.ImageSelectedListener;
import permoveo.com.wikisearch.interfaces.WikiSearchRequestListener;
import permoveo.com.wikisearch.model.SearchPage;
import permoveo.com.wikisearch.network.WikiSearchRequest;
import permoveo.com.wikisearch.util.GridItemDecoration;

/**
 * This fragment simply contains our recyclerview and edit text search field, executes the search request and displays the results
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.rvImageList)
    RecyclerView mRecycler;

    @Bind(R.id.etSearchField)
    EditText mSearchField;

    @Bind(R.id.pbProgress)
    ProgressBar mProgress;

    private ImageSearchAdapter mAdapter;
    private boolean mIsAdapterSet;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    public static final String EXTRA_SEARCH_PAGE = "searchpage";
    public static String EXTRA_VIEW_LEFT = "left";
    public static String EXTRA_VIEW_TOP = "top";
    public static String EXTRA_VIEW_HEIGHT = "height";
    public static String EXTRA_VIEW_WIDTH = "width";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        int spanCount = 3;
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        mRecycler.setHasFixedSize(false);


        mRecycler.addItemDecoration(new GridItemDecoration(4));

        initSearchField();
        return view;
    }


    // Listen for changes in our EditText field, so that we can
    // properly execute a search as the user is typing
    private void initSearchField() {
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("MainActivity", "BeforeTextChanged -> " + s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("MainActivity", "OnTextChanged -> " + s);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d("MainActivity", "After Text Changed -> " + editable.toString());
                search(editable.toString());
            }
        });
    }


    // Make the API call with the supplied query string
    private void search(String query) {

        mProgress.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.INVISIBLE);


        WikiSearchRequest searchRequest = new WikiSearchRequest();
        searchRequest.performSearch(query, new WikiSearchRequestListener() {
            @Override
            public void onSearchRequestCompleted(ArrayList<SearchPage> results) {
                Log.d("MainActivityFragment", "Request finished, count -> " + results.size());

                mRecycler.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.INVISIBLE);

                if (!mIsAdapterSet) {
                    mAdapter = new ImageSearchAdapter(results, new ImageSelectedListener() {
                        @Override
                        public void onImageSelected(SearchPage searchPage, ImageView image) {
                            Log.d("MainActivityFragment", "Image url -> " + searchPage.getImage().getSource());

                            if (searchPage != null) {
                                Log.d("MainActivityFragment", "Search page not null!");
                            }

                            if (searchPage.getImage().getSource() != null && searchPage.getImage().getSource() != null && !searchPage.getImage().getSource().contains(".svg")) {
                                Intent intent = new Intent(getContext(), ImageInfoActivity.class);
                                intent.putExtra(EXTRA_SEARCH_PAGE, searchPage);
                                intent.putExtra("Page Info", captureValues(image));
                                startActivity(intent);
                                getActivity().overridePendingTransition(0, 0);
                            } else {
                                Toast.makeText(getContext(), "Sorry, error loading the image at that URL!", Toast.LENGTH_LONG).show();
                            }


                        }
                    });

                    mRecycler.setAdapter(mAdapter);
                    mIsAdapterSet = true;

                } else {
                    mRecycler.setVisibility(View.VISIBLE);
                    mAdapter.update(results);
                    Log.d("MainActivityFragment", "Updating recycler view");
                }

            }

            @Override
            public void onSearchRequestError(VolleyError error) {

            }
        });
    }

    // Grab our selected ImageView's location and height/width so that we can perform a manual
    // "Shared Element" transition into ImageInfoActivity
    private Bundle captureValues(ImageView image) {
        Bundle b = new Bundle();
        int[] screenLocation = new int[2];
        image.getLocationOnScreen(screenLocation);
        b.putInt(EXTRA_VIEW_LEFT, screenLocation[0]);
        b.putInt(EXTRA_VIEW_TOP, screenLocation[1]);
        b.putInt(EXTRA_VIEW_WIDTH, image.getWidth());
        b.putInt(EXTRA_VIEW_HEIGHT, image.getHeight());


        return b;

    }
}



