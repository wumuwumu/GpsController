package com.sciento.wumu.gpscontroller.Utils;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;

/**
 * Created by wumu on 17-8-15.
 */

public class OtherToAmap {

    public static void googleTOAmap(CurrentLocation currentLocation) {
        LatLng sourceLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        CoordinateConverter converter = new CoordinateConverter(AppContext.getContext());
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        currentLocation.setLatitude(desLatLng.latitude);
        currentLocation.setLongitude(desLatLng.longitude);


    }

}
