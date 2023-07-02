package com.example.travels_map.utils.date

import java.util.concurrent.TimeUnit

enum class TimeGranularity {
    SECONDS {
        override fun toMillis(): Long {
            return TimeUnit.SECONDS.toMillis(1)
        }
    },
    MINUTES {
        override fun toMillis(): Long {
            return TimeUnit.MINUTES.toMillis(1)
        }
    },
    HOURS {
        override fun toMillis(): Long {
            return TimeUnit.HOURS.toMillis(1)
        }
    },
    DAYS {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(1)
        }
    },
    WEEKS {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(7)
        }
    },
    MONTHS {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(30)
        }
    },
    YEARS {
        override fun toMillis(): Long {
            return TimeUnit.DAYS.toMillis(365)
        }
    };

    abstract fun toMillis(): Long
}