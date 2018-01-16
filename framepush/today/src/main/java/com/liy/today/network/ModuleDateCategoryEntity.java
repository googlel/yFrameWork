package com.liy.today.network;

import java.util.List;

/**
 * Created by qj on 2017/5/4.
 */

public class ModuleDateCategoryEntity {


    /**
     * code : 0
     * message : 操作成功
     * errMsg : null
     * data : [{"categoryId":-1,"categoryName":"全部","categorySort":0,"pictureUrl":"http://cdn.oudianyun.com/lyf/branch/back-cms/1487129603965_6639_132.jpg"},{"categoryId":1008021300000010,"categoryName":"坚果炒货","categorySort":17,"pictureUrl":"http://cdn.oudianyun.com/1491041888839_87.22347306653961_79596e09-f762-4986-9e31-1ccb2116e51e.png"},{"categoryId":1006021900000013,"categoryName":"肉类即食","categorySort":18,"pictureUrl":"http://cdn.oudianyun.com/1491041898533_15.727699160609433_a2d5f357-df6a-46b6-85f5-3aff90f72764.png"},{"categoryId":1008022000000047,"categoryName":"糕点饼干","categorySort":20,"pictureUrl":"http://cdn.oudianyun.com/1491041909515_49.939501220738045_8b15a608-376a-43c7-8173-89860ee32d01.png"},{"categoryId":1008022000000040,"categoryName":"果脯蜜饯","categorySort":21,"pictureUrl":"http://cdn.oudianyun.com/1491041917007_4.846599420822095_fd628e73-14d0-40e9-bb95-309b8585f95a.png"},{"categoryId":1008022000000054,"categoryName":"豆菌笋类","categorySort":24,"pictureUrl":"http://cdn.oudianyun.com/1491041923634_14.85929771036022_c2238390-e75e-4e38-9109-ad3602478fb9.png"},{"categoryId":1006022000000066,"categoryName":"糖果果冻","categorySort":25,"pictureUrl":"http://cdn.oudianyun.com/1491041930299_2.237376234654298_60ab5526-a89f-4d93-8050-4c1600177f84.png"},{"categoryId":1007022000000061,"categoryName":"全球尖货","categorySort":27,"pictureUrl":"http://cdn.oudianyun.com/1491041945580_32.16470222802364_ce15b656-3d54-488a-a86c-045c52bf510d.png"}]
     * trace : 80!$1#@18%&10!$,146755,null
     */

    public String code;
    public String message;
    public String errMsg;
    public String trace;
    public String moduleId;
    public List<DataEntity> data;



    public static class DataEntity {
        /**
         * categoryId : -1
         * categoryName : 全部
         * categorySort : 0
         * pictureUrl : http://cdn.oudianyun.com/lyf/branch/back-cms/1487129603965_6639_132.jpg
         */

        public String categoryId;
        public String categoryName;
        public String categorySort;
        public String pictureUrl;
        public boolean choose;
    }
}
