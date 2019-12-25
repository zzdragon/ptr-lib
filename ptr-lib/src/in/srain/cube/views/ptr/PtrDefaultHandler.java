package in.srain.cube.views.ptr;

import android.graphics.Rect;
import android.view.View;
import android.widget.AbsListView;

import androidx.recyclerview.widget.RecyclerView;

public abstract class PtrDefaultHandler implements PtrHandler {

    public static boolean canChildScrollUp(View view) {
        if (view instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) view;
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() < absListView.getPaddingTop());
        } else if (view instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) view).getLayoutManager();
            if (manager.getChildCount() == 0) return false;
            if (manager.canScrollVertically()) {
                View firstVisibleView = manager.getChildAt(0);
                if (firstVisibleView == null) return false;
                int firstVisibleViewPosition = manager.getPosition(firstVisibleView);
                int maxTopPadding = 0;
                for (int i = 0; i < ((RecyclerView) view).getItemDecorationCount(); i++) {
                    RecyclerView.ItemDecoration itemDecoration = ((RecyclerView) view).getItemDecorationAt(i);
                    Rect outRect = new Rect();
                    itemDecoration.getItemOffsets(outRect, firstVisibleViewPosition, (RecyclerView) view);
                    maxTopPadding = Math.max(maxTopPadding, outRect.top);
                }
                return firstVisibleViewPosition > 0 || firstVisibleView.getTop() + maxTopPadding < view.getPaddingTop();
            } else {
                return true;
            }
        } else {
            return view.getScrollY() > 0;
        }
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledDown(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return checkContentCanBePulledDown(frame, content, header);
    }

}