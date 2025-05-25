package com.design.appproject.ui.shopaddress;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.design.appproject.R;
import com.design.appproject.base.CommonArouteApi;
import com.design.appproject.widget.CommonTitleBarView;
import com.union.union_basic.logger.LoggerManager;

import java.util.List;

/**
 * 地址地图定位
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDRESS_MAP)
public class DragLocationViewMap extends Activity implements AMap.OnMarkerClickListener,
        AMap.OnMapLoadedListener, AMap.OnMapClickListener, LocationSource, AMapLocationListener,
        GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener,Inputtips.InputtipsListener {
    private MapView mMapView;
    private EditText mSearchEt;
    private RecyclerView mRecyclerView;
    private AMap mAMap;
    private Marker mGPSMarker;//定位位置显示
    private AMapLocation  location;
    private LocationSource.OnLocationChangedListener mListener;//声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private GeocodeSearch geocoderSearch;
    private MarkerOptions markOptions;
    private LatLng latLng;
    private String addressName;
    private SearchListAdapter mSearchListAdapter;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);
        setContentView(R.layout.activity_drag_location_view_map);
        CommonTitleBarView barView = findViewById(R.id.barview);
        barView.getMRightTextView().setVisibility(View.VISIBLE);
        barView.getMRightTextView().setText("确定");
        barView.getMRightTextView().setTextColor(ColorUtils.getColor(R.color.common_bg_color));
        barView.getMRightTextView().setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("address",mGPSMarker!=null?mGPSMarker.getSnippet():"");
            intent.putExtra("latitude",mGPSMarker!=null?mGPSMarker.getPosition().latitude:"");
            intent.putExtra("longitude",mGPSMarker!=null?mGPSMarker.getPosition().longitude:"");
            setResult(RESULT_OK,intent);
            finish();
        });
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView = findViewById(R.id.map_view);
        mSearchEt = findViewById(R.id.search_et);
        mRecyclerView = findViewById(R.id.address_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchListAdapter = new SearchListAdapter();
        mSearchListAdapter.setOnItemClickListener((adapter, view, position) -> {
            LatLonPoint latLonPoint = mSearchListAdapter.getData().get(position).getPoint();
            mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()),17,30,0)));
        });
        mRecyclerView.setAdapter(mSearchListAdapter);
        mMapView.onCreate(savedInstanceState);
        try {
            geocoderSearch = new GeocodeSearch(this);
        } catch (AMapException e) {
            throw new RuntimeException(e);
        }
        mSearchEt.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                InputtipsQuery inputquery = new InputtipsQuery(editable.toString().trim(), "");
                inputquery.setCityLimit(true);
                Inputtips inputTips = new Inputtips(DragLocationViewMap.this, inputquery);
                inputTips.setInputtipsListener(DragLocationViewMap.this);
                inputTips.requestInputtipsAsyn();
            }
        });
        mAMap = mMapView.getMap();
        // 设置定位监听
        mAMap.setOnMapLoadedListener(this);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);
        //设置地图拖动监听
        mAMap.setOnCameraChangeListener(this);
        //逆编码监听事件
        geocoderSearch.setOnGeocodeSearchListener(this);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17)); //缩放比例
        //添加一个圆
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.radius(20.0f);
        mAMap.addCircle(circleOptions);
        //设置amap的属性
        UiSettings settings = mAMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        location = aMapLocation;
        if (mListener != null && location != null) {
            if (location.getErrorCode() == 0) {
                mListener.onLocationChanged(location);// 显示系统箭头
                LatLng la = new LatLng(location.getLatitude(), location.getLongitude());
                setMarket(la, location.getCity(), location.getAddress());
                mLocationClient.stopLocation();
            }
        } else {
            ToastUtils.showShort("定位失败");
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000 * 10);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng latLonPoint = new LatLng(latLng.latitude, latLng.longitude);
        if (!TextUtils.isEmpty(latLonPoint.toString())) {
            getAddress(latLonPoint);
        } else {
            ToastUtils.showShort("拜访地址获取失败");
        }
    }

    @Override
    public void onMapLoaded() {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// 销毁定位
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mMapView.onDestroy();
    }

    private void setMarket(LatLng latLng, String title, String content) {
        if (mGPSMarker != null) {
            mGPSMarker.remove();
        }
        //获取mapview宽高
        int width = (mMapView.getMeasuredWidth()) / 2;
        int height = (mMapView.getMeasuredHeight()/ 2) - 80;
        markOptions = new MarkerOptions();
        markOptions.draggable(true);//设置Marker可拖动
        markOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), com.permissionx.guolindev.R.drawable.permissionx_ic_location))).anchor(0.5f, 0.7f);
        //设置一个角标
        mGPSMarker = mAMap.addMarker(markOptions);
        //设置marker在屏幕的像素坐标
        mGPSMarker.setPosition(latLng);
        mGPSMarker.setTitle(title);
        mGPSMarker.setSnippet(content);
        //设置像素坐标
        mGPSMarker.setPositionByPixels(width, height);
        if (!TextUtils.isEmpty(content)) {
            mGPSMarker.showInfoWindow();
        }
        mMapView.invalidate();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latLng=cameraPosition.target;
        getAddress(latLng);
    }
    /**
     * 根据经纬度得到地址
     */
    public void getAddress(final LatLng latLng) {
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress(); // 逆转地里编码不是每次都可以得到对应地图上的opi
                Log.e("逆地理编码回调  得到的地址：" , addressName);
                setMarket(latLng, location.getCity(), addressName);
            }
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        Log.d("onGeocodeSearched",String.format("%s___rCode:%d", result.toString(), rCode));
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        mSearchListAdapter.setNewInstance(list);
        LoggerManager.INSTANCE.d(String.format("onGetInputtips:", list.toString()),"onGetInputtips");
    }
}