package com.othman.a7minuteworkout

import android.app.Application

class WorkoutApp:Application() {

    val db by lazy {
        HistoryDatabase.getInstance(this)
    }

}