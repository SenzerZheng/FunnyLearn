package net.frontdo.funnylearn.common;

import android.hardware.Camera;
import android.os.Build;

/**
 * ProjectName: CameraUtils
 * Description: 摄像头工具：判断是否具有，前置或后置摄像头
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/20/2016 19:09
 */
public class CameraUtils {
    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
}
