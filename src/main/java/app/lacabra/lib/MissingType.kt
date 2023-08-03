package app.lacabra.lib

@Suppress("unused")
enum class MissingType {
    COMMAND {
        override val code: String = "command"
        override val placeholder: String = "en un comando"
        override val color: Int = 0xDB566B
    },
    EVENT {
        override val code: String = "event"
        override val placeholder: String = "en un evento"
        override val color: Int = 0x51F09A
    },
    CONFIG {
        override val code: String = "config"
        override val placeholder: String = "en la configuración"
        override val color: Int = 0x7E4BDE
    },
    OTHER {
        override val code: String = "other"
        override val placeholder: String = "en otro bug"
        override val color: Int = 0x000000
    };

    abstract val code: String
    abstract val placeholder: String
    abstract val color: Int
}