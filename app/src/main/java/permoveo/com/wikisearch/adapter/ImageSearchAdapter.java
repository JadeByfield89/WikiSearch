package permoveo.com.wikisearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.wikisearch.R;
import permoveo.com.wikisearch.interfaces.ImageSelectedListener;
import permoveo.com.wikisearch.model.SearchPage;

/**
 * Created by byfieldj on 5/22/17.
 */

public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.ViewHolder> {

    private static final String TAG = "ImageSearchAdapter";
    private ArrayList<SearchPage> mResults;
    private ImageSelectedListener mListener;

    public ImageSearchAdapter(ArrayList<SearchPage> results, ImageSelectedListener listener) {
        this.mResults = results;
        this.mListener = listener;
    }

    // Clear the dataset and append the new results to it each time a search is initiated
    public void update(ArrayList<SearchPage> newResults) {
        mResults.clear();
        mResults.addAll(newResults);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page_result, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onImageSelected(mResults.get(position), holder.mImage);

            }
        });


        Log.d(TAG, "Image source -> " + mResults.get(position).getImage().getSource());
        Picasso.with(holder.mImage.getContext()).load(mResults.get(position).getImage().getSource()).fit().centerCrop().placeholder(R.drawable.placeholder_thumbnail).into(holder.mImage);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivImage)
        ImageView mImage;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
