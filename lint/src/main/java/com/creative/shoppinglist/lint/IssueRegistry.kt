package com.creative.shoppinglist.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

class IssueRegistry : IssueRegistry() {
    override val issues = listOf(ComposeTestRuleUsageDetector.ISSUE)
    override val api: Int = CURRENT_API
    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Creative Shopping List",
        feedbackUrl = "https://github.com/creative/shoppinglist/issues",
        contact = "https://github.com/creative/shoppinglist"
    )
}
