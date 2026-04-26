package pam.tugas5.romadhon

actual class BatteryInfo {
    actual fun getBatteryLevel(): Int {
        return 100
    }

    actual fun isCharging(): Boolean {
        return false
    }
}