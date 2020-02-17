package br.com.gabriellferreira.carlist.presentation.util.extension

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import br.com.gabriellferreira.carlist.domain.model.Retryable
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.showRetrySnackbar(parentView: View?, text: String, retryable: Retryable) {
    parentView?.let {
        val actionColor = ResourcesCompat.getColor(resources, android.R.color.holo_orange_light, theme)
        Snackbar.make(parentView, text, Snackbar.LENGTH_INDEFINITE)
            .setActionTextColor(actionColor)
            .setAction("RETRY") {
                retryable.retry()
            }
            .show()
    }
}