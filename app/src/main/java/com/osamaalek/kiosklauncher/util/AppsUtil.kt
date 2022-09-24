package com.osamaalek.kiosklauncher.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.osamaalek.kiosklauncher.model.AppInfo


class AppsUtil {

    companion object {

        fun getAllApps(context: Context): List<AppInfo> {
            val packageManager: PackageManager = context.packageManager
            val appsList = ArrayList<AppInfo>()
            val i = Intent(Intent.ACTION_MAIN, null)
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            val allApps = packageManager.queryIntentActivities(i, 0)
            for (ri in allApps) {
                val app = AppInfo(
                    ri.loadLabel(packageManager),
                    ri.activityInfo.packageName,
                    ri.activityInfo.loadIcon(packageManager)
                )
                appsList.add(app)
            }
            return appsList
        }

    }
}