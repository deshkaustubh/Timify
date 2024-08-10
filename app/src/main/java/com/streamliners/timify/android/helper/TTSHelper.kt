package com.streamliners.timify.android.helper

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.*
import android.speech.tts.UtteranceProgressListener
import com.streamliners.base.exception.BusinessException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TTSHelper(
    private val context: Context
) {

    private var tts: TextToSpeech? = null

    suspend fun speak(text: String) {
        if (tts == null) {
            init(context, text)
        } else {
            speakText(text)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.shutdown(); tts = null
    }

    private suspend fun init(context: Context, text: String) {
        return suspendCoroutine { cont ->
            tts = TextToSpeech(context) { status ->
                if (status == SUCCESS) {
                    val result: Int = tts!!.setLanguage(Locale.US)
                    if (result != LANG_MISSING_DATA && result != LANG_NOT_SUPPORTED) {
                        CoroutineScope(cont.context).launch {
                            speakText(text)
                            cont.resume(Unit)
                        }
                    } else {
                        error("TTS Error Code : $result")
                    }
                } else {
                    error("TTS Init Error")
                }
            }
        }
    }

    private suspend fun speakText(text: String) {
        return suspendCoroutine { cont ->
            tts?.speak(text, QUEUE_FLUSH, null, "1")
            tts?.setOnUtteranceProgressListener(
                object : UtteranceProgressListener() {
                    override fun onDone(utteranceId: String?) {
                        cont.resume(Unit)
                    }
                    override fun onStart(utteranceId: String?) {}
                    override fun onError(utteranceId: String?) {
                        cont.resumeWithException(BusinessException("TTS Error"))
                    }
                }
            )
        }
    }

}