package app.kwawannan.com.ispacestation.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.kwawannan.com.ispacestation.R;
import app.kwawannan.com.ispacestation.model.PassTime;
import app.kwawannan.com.ispacestation.utils.ISSUtils;


/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public class ISSAdapter extends RecyclerView.Adapter<ISSAdapter.ISSViewHolder> {

    private static final String TAG = ISSAdapter.class.getSimpleName();
    private Context mContext;
    private Vector<PassTime> mAdapterItemList = new Vector<PassTime>();
    private View mView;

    public ISSAdapter(Activity mContext, Vector<PassTime>aAdapterItemList) {
        this.mContext = mContext;
        this.mAdapterItemList = aAdapterItemList;
    }

    @Override
    public ISSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.passtime_rcylr_item, parent, false);
        return new ISSViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ISSViewHolder holder, int position) {
        Log.d(TAG , "onBindViewHolder" );
        if (holder!=null  && holder instanceof ISSViewHolder ) {
             if (position == mAdapterItemList.size()) {
                holder.mItemLayout.setVisibility(View.GONE);
                return;
            }

            String timeString = ISSUtils.getInstance().convertSec2HrMin(mAdapterItemList.get(position).getDuration());
            String date = ISSUtils.getInstance().convertLong2Date(mAdapterItemList.get(position).getRisetime());

            holder.duration.setText(timeString);
            holder.riseTime.setText(date);

        }

    }
    @Override
    public void onViewAttachedToWindow(ISSViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }
    @Override
    public int getItemCount() {
        if (mAdapterItemList.size()!=0)
            return mAdapterItemList.size() + 1;
        else
            return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public void updateAdapterList(Vector<PassTime> aRecyclerItemList) {
        this.mAdapterItemList = aRecyclerItemList;
        this.notifyDataSetChanged();
    }

    public class ISSViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.duration)
        TextView duration;
        @BindView(R.id.risetime)
        TextView riseTime;
        @BindView(R.id.item_layout)
        RelativeLayout mItemLayout;

        public ISSViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
