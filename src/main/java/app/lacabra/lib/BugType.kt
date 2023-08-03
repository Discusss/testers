package app.lacabra.lib

@Suppress("unused")
enum class BugType {
    DISCORD {
        override val code: String = "discord"
        override val placeholder: String = "con Discord"
        override val color: Int = 0x5B73F5
    },
    MODERATION {
        override val code: String = "moderation"
        override val placeholder: String = "con un comando de moderación"
        override val color: Int = 0xF08E51
    },
    COMMAND {
        override val code: String = "command"
        override val placeholder: String = "con un comando"
        override val color: Int = 0xDB566B
    },
    EVENT {
        override val code: String = "event"
        override val placeholder: String = "con un evento"
        override val color: Int = 0x51F09A
    },
    CONFIG {
        override val code: String = "config"
        override val placeholder: String = "con la configuración"
        override val color: Int = 0x7E4BDE
    },
    APPEAL {
        override val code: String = "appeal"
        override val placeholder: String = "con las apelaciones"
        override val color: Int = 0x4BDE65
    },
    INFRACTIONS {
        override val code: String = "infractions"
        override val placeholder: String = "con el sistema de infracciones"
        override val color: Int = 0xFF858F
    },
    OTHER {
        override val code: String = "other"
        override val placeholder: String = "con otro bug"
        override val color: Int = 0x000000
    };

    abstract val code: String
    abstract val placeholder: String
    abstract val color: Int
}