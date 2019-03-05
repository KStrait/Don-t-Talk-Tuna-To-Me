package com.kylestrait.donttalktunatome.data

class Constants {
    interface ACTION {
        companion object {
            val MAIN_ACTION = "com.truiton.foregroundservice.action.main"
            val PREV_ACTION = "com.truiton.foregroundservice.action.prev"
            val PLAY_ACTION = "com.truiton.foregroundservice.action.play"
            val NEXT_ACTION = "com.truiton.foregroundservice.action.next"
            val STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground"
            val STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground"
        }
    }

    interface NOTIFICATION_ID {
        companion object {
            val FOREGROUND_SERVICE = 101
        }
    }
}