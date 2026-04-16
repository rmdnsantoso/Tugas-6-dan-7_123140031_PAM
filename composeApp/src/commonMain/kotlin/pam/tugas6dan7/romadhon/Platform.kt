package pam.tugas6dan7.romadhon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform