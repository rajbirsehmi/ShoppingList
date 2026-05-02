package com.creative.shoppinglist.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UVariable

class ComposeTestRuleUsageDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UVariable::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitVariable(node: UVariable) {
                val type = node.type.canonicalText
                if (type.contains("androidx.compose.ui.test.junit4.ComposeTestRule") ||
                    type.contains("androidx.compose.ui.test.junit4.AndroidComposeTestRule")) {
                    
                    // Flag usage if not in core-testing module
                    if (context.project.name != "core-testing") {
                        context.report(
                            ISSUE,
                            node as UElement,
                            context.getLocation(node as UElement),
                            "Direct usage of ComposeTestRule is restricted to :core-testing. Use the TestingEngine DSL instead."
                        )
                    }
                }
            }
        }
    }

    companion object {
        val ISSUE = Issue.create(
            id = "ComposeTestRuleUsage",
            briefDescription = "Restrict ComposeTestRule usage",
            explanation = "To maintain a robust testing architecture, ComposeTestRule should only be used within the :core-testing module's Robots. Other modules should use the TestingEngine DSL.",
            category = Category.CORRECTNESS,
            priority = 7,
            severity = Severity.ERROR,
            implementation = Implementation(
                ComposeTestRuleUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
