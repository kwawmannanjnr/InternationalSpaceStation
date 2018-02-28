package app.kwawannan.com.ispacestation.view.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;


/**
 * Created by Bikash.K on 1/5/2016.
 */
public class ISSLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;
    private static final String TAG = "iss.chase.com.ispacestation.ui.widget.ISSLinearLayoutManager";



    public ISSLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        //return isScrollEnabled && super.canScrollVertically();
        return isScrollEnabled;
    }


}
