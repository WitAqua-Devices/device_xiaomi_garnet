/*
 * Copyright (C) 2021 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.felica

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.util.Log
import android.os.SystemProperties

object FelicaDisabler {
    private const val TAG = "XiaomiFelicaDisabler"

    private val FELICA_PACKAGES = listOf(
        "com.felicanetworks.mfc",
    )

    private fun isInstalledAndEnabled(pm: PackageManager, pkgName: String) = runCatching {
        val info = pm.getPackageInfo(pkgName, PackageInfoFlags.of(0))
        Log.d(TAG, "package $pkgName installed, enabled = ${info.applicationInfo?.enabled}")
        info.applicationInfo?.enabled ?: false
    }.getOrDefault(false)

    fun enableOrDisableFelica(context: Context) {
        val pm = context.packageManager
        val sku = SystemProperties.get("ro.boot.product.hardware.sku")
        val disable = if (!(sku == "JP")) {
            Log.d(TAG, "Disabling apps due not to JP SKU")
            true // Disable if not JP SKU
        } else {
            false
        }
        val flag = if (disable) {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        }

        for (pkg in FELICA_PACKAGES) {
            pm.setApplicationEnabledSetting(pkg, flag, 0)
        }
    }
}
