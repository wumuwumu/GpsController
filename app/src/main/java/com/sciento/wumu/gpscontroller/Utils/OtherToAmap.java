package com.sciento.wumu.gpscontroller.Utils;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.sciento.wumu.gpscontroller.CommonModule.AppContext;

/**
 * Created by wumu on 17-8-15.
 */

public class OtherToAmap {

    public static LatLng googleTOAmap(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter(AppContext.getContext());
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GOOGLE);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

}
