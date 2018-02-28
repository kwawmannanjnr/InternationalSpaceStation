package app.kwawannan.com.ispacestation.view;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

import app.kwawannan.com.ispacestation.R;
import app.kwawannan.com.ispacestation.presenter.ISSPresenterImp;
import app.kwawannan.com.ispacestation.presenter.gps.GPSManager;
import app.kwawannan.com.ispacestation.presenter.gps.GPSService;
import app.kwawannan.com.ispacestation.model.PassTime;
import app.kwawannan.com.ispacestation.model.SpaceStationData;
import app.kwawannan.com.ispacestation.view.adapter.ISSAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import app.kwawannan.com.ispacestation.view.widget.ISSLinearLayoutManager;
import app.kwawannan.com.ispacestation.utils.ISSUtils;

/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public class ISSFragment extends Fragment implements IISSView , ILocationChanged{


    private ISSAdapter mIssAdapter;
    private Vector<PassTime> mRecyclerItemList = new Vector<>();
    GPSManager mGpsTracker;
    ISSPresenterImp preseterimpl;
    Activity activity ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ResponseParser.registerListner(this);
        preseterimpl = new ISSPresenterImp(this);
        GPSManager.registerListner(this);
        GPSService.registerListner(this);
    }

    public static final String EXTRA_ISS_DURATION = "EXTRA_ISS_DURATION";
    public static final String EXTRA_ISS_RISETIME = "EXTRA_ISS_RISETIME";

    @BindView(R.id.txtview_welcome_name)
    TextView welcomename ;
    @BindView(R.id.latlong)
    TextView latlong;
    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerViewScroll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lView = inflater.inflate(R.layout.passtime_view, container, false);
        ButterKnife.bind(this,lView);
        welcomename.setText("Welcom to Space Station!! ");
        prepareRecyclerView();
        return lView;
    }

    private void prepareRecyclerView() {
        mIssAdapter = new ISSAdapter(getActivity(), mRecyclerItemList);

        ISSLinearLayoutManager wmLayout = new ISSLinearLayoutManager(getContext());
        mRecyclerViewScroll.setLayoutManager(wmLayout);

        mRecyclerViewScroll.setAdapter(mIssAdapter);
        mRecyclerViewScroll.setNestedScrollingEnabled(true);
        mRecyclerViewScroll.setHasFixedSize(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void notifyDataChange(final Vector<SpaceStationData> spaceStationData) {
        getActivity().runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                latlong.setText(spaceStationData.get(0).getRequest().getLatitude() + "," + spaceStationData.get(0).getRequest().getLongitude() + "Alt:" + spaceStationData.get(0).getRequest().getAltitude());
                datetime.setText(ISSUtils.getInstance().convertLong2Date(spaceStationData.get(0).getRequest().getDatetime()));
                if (spaceStationData.get(0).getResponse() != null && spaceStationData.get(0).getResponse() .length > 0) {
                    mRecyclerItemList.clear();

                    for (PassTime aPassTime : spaceStationData.get(0).getResponse()) {
                        mRecyclerItemList.add(aPassTime);
                    }
                    if (mRecyclerItemList.size() > 0) {
                        mIssAdapter.updateAdapterList(mRecyclerItemList);
                    }
                }
            }

        }));
    }

    @Override
    public void onLocationChanged(final Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        preseterimpl.getPassTime(location);

    }
}
