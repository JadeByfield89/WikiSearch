package permoveo.com.wikisearch.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by byfieldj on 4/28/16.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpacing;

    public GridItemDecoration(final int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = mSpacing;
        outRect.right = mSpacing;
        outRect.bottom = mSpacing;
        outRect.top = mSpacing;

    }
}
