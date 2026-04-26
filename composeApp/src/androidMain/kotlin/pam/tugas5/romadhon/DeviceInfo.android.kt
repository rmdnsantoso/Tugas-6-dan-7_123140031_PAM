package pam.tugas5.romadhon

import android.os.Build

actual class DeviceInfo {
    actual fun getDeviceName(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    actual fun getOsVersion(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    actual fun getAppVersion(): String {
        return "1.0.0"
    }
}