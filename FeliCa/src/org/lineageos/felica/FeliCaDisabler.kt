/*
 * Copyright (C) 2025 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.felica

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.os.SystemProperties

object FeliCaDisabler {
    private const val TAG = "XiaomiFeliCaDisabler"

    private val FELICA_PACKAGES = listOf(
        "com.felicanetworks.mfm.main",
        "com.felicanetworks.mfc",
        "com.felicanetworks.mfs",
        "com.felicanetworks.mfw.a.boot",
    )

    fun enableOrDisableFeliCa(context: Context) {
        val pm = context.packageManager
        val sku = SystemProperties.get("ro.boot.product.hardware.sku")
        val disable = sku != "JP"
        if (disable) {
            Log.d(TAG, "Disabling FeliCa apps (non-JP SKU)")
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
