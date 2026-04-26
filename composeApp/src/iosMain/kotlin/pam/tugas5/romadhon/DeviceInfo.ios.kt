package pam.tugas5.romadhon

actual class DeviceInfo {
    actual fun getDeviceName(): String {
        return "iOS Device"
    }

    actual fun getOsVersion(): String {
        return "iOS Version"
    }

    actual fun getAppVersion(): String {
        return "1.0.0"
    }
}