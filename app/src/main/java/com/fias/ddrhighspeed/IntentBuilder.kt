package com.fias.ddrhighspeed

import android.content.Intent
import android.net.Uri

class IntentBuilder {
    fun createPrivacyPolicyIntent(): Intent {
        val webpage: Uri = Uri.parse(POLICY_URL)
        return Intent(Intent.ACTION_VIEW, webpage)
    }

    fun createInquiryMailIntent(): Intent {
        val uri = Uri.parse(MAIL_URI)
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(MAIL_ADDRESS))
            putExtra(Intent.EXTRA_SUBJECT, MAIL_SUBJECT)
            putExtra(
                Intent.EXTRA_TEXT,
                MAIL_TEXT
            )
        }

        return intent
    }

    companion object {
        const val POLICY_URL = "https://fia1988.github.io/PrivacyPolicy"

        const val MAIL_URI = "mailto:"
        const val MAIL_ADDRESS = "apps.fias@gmail.com"
        const val MAIL_SUBJECT = "お問い合わせ"
        const val MAIL_TEXT = "ーーーーーーーーーーーーー\n" +
                "ご要望・不具合・その他なんでもご連絡ください。\n" +
                "ーーーーーーーーーーーーー\n"
    }

}