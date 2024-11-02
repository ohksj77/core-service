package com.dragonguard.core.support.docs

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors

class RestDocsUtils {
    companion object {
        fun getDocumentRequest(): OperationRequestPreprocessor = Preprocessors.preprocessRequest(Preprocessors.prettyPrint())

        fun getDocumentResponse(): OperationResponsePreprocessor = Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    }
}
