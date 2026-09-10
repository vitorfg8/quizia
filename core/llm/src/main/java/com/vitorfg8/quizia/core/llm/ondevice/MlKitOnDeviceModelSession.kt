package com.vitorfg8.quizia.core.llm.ondevice

import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel
import kotlinx.coroutines.flow.lastOrNull

/** Adapter over the ML Kit GenAI Prompt API, which serves Gemini Nano through AICore. */
internal class MlKitOnDeviceModelSession : OnDeviceModelSession {

    private val model: GenerativeModel by lazy { Generation.getClient() }

    override suspend fun isSupported(): Boolean = model.checkStatus() != FeatureStatus.UNAVAILABLE

    override suspend fun prepare(): Boolean = when (model.checkStatus()) {
        FeatureStatus.AVAILABLE -> true
        FeatureStatus.DOWNLOADABLE -> model.download().lastOrNull() is DownloadStatus.DownloadCompleted
        else -> false
    }

    override suspend fun generate(prompt: String): String? =
        model.generateContent(prompt).candidates.firstOrNull()?.text
}
