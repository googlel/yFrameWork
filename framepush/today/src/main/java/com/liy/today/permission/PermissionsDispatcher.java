package com.liy.today.permission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public final class PermissionsDispatcher {

    private PermissionsDispatcher() {
    }

    /**
     * check permissions are whether granted or not
     * @param act act
     * @param requestCode requestCode
     * @param listener listener
     * @param permissions permissions
     */
    public static void checkPermissions(Activity act, int requestCode
            , PermissionListener listener, String... permissions) {

        if (act == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null
                        , "checkPermissions()-->param act :the activity is null", permissions);
            }
            return;
        }
        if (permissions == null || permissions.length < 1) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null
                        , "checkPermissions()-->param permissions: is null or length is 0", permissions);
            }
            return;
        }

        /*****
         * check permissions are granted ?
         * */
        PermissionUtils.sortGrantedAndDeniedPermissions(act, permissions);

        if(PermissionUtils.getGrantedPermissions().size() > 0) {
            List<String> grantedPermissionsList = PermissionUtils.getGrantedPermissions();
            String[] grantedPermissionsArr =
                    grantedPermissionsList.toArray(new String[grantedPermissionsList.size()]);
            if (listener != null) {
                listener.onPermissionsGranted(requestCode, null, grantedPermissionsArr);
            }
        }

        if(PermissionUtils.getDeniedPermissions().size() > 0) {
            List<String> deniedPermissionsList = PermissionUtils.getDeniedPermissions();
            String[] deniedPermissionsArr =
                    deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
            if(deniedPermissionsArr.length > 0 ) {
                PermissionUtils.sortUnshowPermission(act,deniedPermissionsArr );
            }
        }

        if(PermissionUtils.getUnshowedPermissions().size() > 0) {
            List<String> unShowPermissionsList = PermissionUtils.getUnshowedPermissions();
            String[] unShowPermissionsArr =
                    unShowPermissionsList.toArray(new String[unShowPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, false, unShowPermissionsArr);
            }
        }

        if(PermissionUtils.getNeedRequestPermissions().size() > 0 ) {//true 表示允许弹申请权限框
            List<String> needRequestPermissionsList = PermissionUtils.getNeedRequestPermissions();
            String[] needRequestPermissionsArr =
                    needRequestPermissionsList.toArray(new String[needRequestPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, true, needRequestPermissionsArr);
            }
        }
    }


    /**
     *   permissions to be granted
     * @param act act
     * @param requestCode requestCode
     * @param permissions permissions
     */
    public static void requestPermissions(Activity act, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(act, permissions, requestCode);
    }


    /**
     * do their permissions results
     * @param act act
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     * @param listener listener
     */
    public static void onRequestPermissionsResult(Activity act, int requestCode
            , String[] permissions, int[] grantResults, PermissionListener listener) {

        if (act == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, grantResults
                        , "onRequestPermissionsResult()-->param act :the activity is null", permissions);
            }
            return;
        }

        if (PermissionUtils.verifyPermissions(grantResults)) {
            if (listener != null) {
                listener.onPermissionsGranted(requestCode, grantResults, permissions);
            }
        } else {
            if (listener != null) {
                listener.onPermissionsDenied(requestCode, grantResults, permissions);
            }
        }
    }



    /**
     * check permissions are whether granted or not for fragment
     * @param  fragment fragment
     * @param  requestCode requestCode
     * @param  listener listener
     * @param  permissions permissions
     */
    public static void checkPermissionsByFragment(Fragment fragment, int requestCode
            , PermissionListener listener, String... permissions) {

        if (fragment == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null
                        , "checkPermissions()-->param act :the activity is null", permissions);
            }
            return;
        }
        if (permissions == null || permissions.length < 1) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null
                        , "checkPermissions()-->param permissions: is null or length is 0", permissions);
            }
            return;
        }

        PermissionUtils.sortGrantedAndDeniedPermissions(fragment.getContext(), permissions);

        if(PermissionUtils.getGrantedPermissions().size() > 0) {
            List<String> grantedPermissionsList = PermissionUtils.getGrantedPermissions();
            String[] grantedPermissionsArr = grantedPermissionsList.toArray(new String[grantedPermissionsList.size()]);

            if (listener != null) {
                listener.onPermissionsGranted(requestCode, null, grantedPermissionsArr);
            }
        }

        if(PermissionUtils.getDeniedPermissions().size() > 0) {
            List<String> deniedPermissionsList = PermissionUtils.getDeniedPermissions();
            String[] deniedPermissionsArr = deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
            if(deniedPermissionsArr.length > 0 ) {
                PermissionUtils.sortUnshowPermissionByFragment(fragment,deniedPermissionsArr );
            }
        }

        if(PermissionUtils.getUnshowedPermissions().size() > 0) {
            List<String> unShowPermissionsList = PermissionUtils.getUnshowedPermissions();
            String[] unShowPermissionsArr = unShowPermissionsList.toArray(new String[unShowPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, false, unShowPermissionsArr);
            }
        }

        if(PermissionUtils.getNeedRequestPermissions().size() > 0 ) {//true 表示允许弹申请权限框
            List<String> needRequestPermissionsList = PermissionUtils.getNeedRequestPermissions();
            String[] needRequestPermissionsArr = needRequestPermissionsList.toArray(new String[needRequestPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, true, needRequestPermissionsArr);
            }
        }
    }

    /**
     * request permissions to be granted for fragment
     * @param fragment fragment
     * @param requestCode requestCode
     * @param permissions permissions
     */
    public static void requestPermissionsByFragment(Fragment fragment, int requestCode, String... permissions) {
        fragment.requestPermissions(permissions, requestCode);
    }


    /**
     * do their permissions results for fragment
     * @param fragment fragment
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     * @param listener listener
     */
    public static void onRequestPermissionsResultByFragment(Fragment fragment, int requestCode, String[] permissions, int[] grantResults, PermissionListener listener) {

        if (fragment == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, grantResults, "onRequestPermissionsResult()-->param act :the activity is null", permissions);
            }
            return;
        }

        if (PermissionUtils.verifyPermissions(grantResults)) {

            if (listener != null) {
                listener.onPermissionsGranted(requestCode, grantResults, permissions);
            }
        } else {
            if (listener != null) {
                listener.onPermissionsDenied( requestCode, grantResults, permissions);
            }
        }
    }
}

