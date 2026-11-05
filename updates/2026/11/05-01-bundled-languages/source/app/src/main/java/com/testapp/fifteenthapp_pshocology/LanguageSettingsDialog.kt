package com.testapp.fifteenthapp_pshocology

import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class LanguageSettingsDialog : DialogFragment() {
    private lateinit var model: LanguageSettingsViewModel
    private var selectedCode = "ko"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        model = ViewModelProvider(this)[LanguageSettingsViewModel::class.java]
        selectedCode = savedInstanceState?.getString("selected") ?: L.language
        val context = requireContext()
        val body = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 24)
        }
        val selection = RadioGroup(context)
        L.codes.forEachIndexed { index, code ->
            selection.addView(RadioButton(context).apply {
                id = index + 1000
                text = L.names[index]
                minHeight = (48 * resources.displayMetrics.density).toInt()
                isChecked = code == selectedCode
            })
        }
        selection.setOnCheckedChangeListener { _, id ->
            if (id >= 1000 && id - 1000 < L.codes.size) {
                selectedCode = L.codes[id - 1000]
            }
        }
        body.addView(selection)

        val dialog = AlertDialog.Builder(context)
            .setTitle(L.text("앱 언어"))
            .setView(ScrollView(context).apply { addView(body) })
            .setPositiveButton(L.text("적용")) { _, _ ->
                model.start(selectedCode, true)
                if (model.applyPrepared()) {
                    dismiss()
                    requireActivity().recreate()
                }
            }
            .setNegativeButton(L.text("취소"), null)
            .create()

        return dialog
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selected", selectedCode)
    }
}
